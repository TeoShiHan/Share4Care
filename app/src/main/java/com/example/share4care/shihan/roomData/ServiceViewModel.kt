package com.example.share4care.shihan.roomData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.share4care.contentData.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServiceViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<OKUserviceDB>>
    private val repository: ServiceRepository
    private val roomCaster = TypeCaster()

    init {
        val serviceDao = ServiceDatabase.getDatabase(application).serviceDao()
        repository = ServiceRepository(serviceDao)
        readAllData = repository.readAllData
    }

    fun addService(service: OKUserviceDB){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addService(service)
        }
    }
    fun iterateConvertAndAdd(serviceList : MutableList<Service>){
        for(singleService in serviceList){
            val tempTravel = roomCaster.getServiceData(singleService)
            addService(tempTravel)
        }
    }
}