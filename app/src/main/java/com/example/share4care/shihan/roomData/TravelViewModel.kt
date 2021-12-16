package com.example.share4care.shihan.roomData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Travel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TravelViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<TravelDB>>
    private val repository: TravelRepository
    private val roomCaster = TypeCaster()

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
}