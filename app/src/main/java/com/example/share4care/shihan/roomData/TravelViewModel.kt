package com.example.share4care.shihan.roomData

import android.app.Application
import androidx.lifecycle.*
import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Travel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.acl.Owner

class TravelViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<TravelDB>>
    private val repository: TravelRepository
    private val roomCaster = TypeCaster()

    var travelTable: List<TravelDB>? = null

    init {
        val travelDao = TravelDatabase.getDatabase(application).travelDao()
        repository = TravelRepository(travelDao)
        readAllData = repository.readAllData
    }

    fun addTravel(travel: TravelDB){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTravel(travel)
        }
    }

    fun iterateConvertAndAdd(travelList : MutableList<Travel>){
        for(singleTravel in travelList){
            val tempTravel = roomCaster.getTravelData(singleTravel)
            addTravel(tempTravel)
        }
    }

    private fun letDataFlowFromDatabaseToGlobalVariable(owner: LifecycleOwner){
        readAllData.observe(owner, Observer {
            data -> travelTable = data
        })
    }

    fun fetchTravelTableData(owner: LifecycleOwner): List<TravelDB>? {
        letDataFlowFromDatabaseToGlobalVariable(owner)
        return travelTable
    }
}