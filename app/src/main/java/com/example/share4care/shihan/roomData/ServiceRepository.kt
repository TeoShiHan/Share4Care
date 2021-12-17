package com.example.share4care.shihan.roomData

import androidx.lifecycle.LiveData

class ServiceRepository(private val serviceDao: ServiceDao) {
    val readAllData: LiveData<List<OKUserviceDB>> = serviceDao.readAllData()

    suspend fun addService(service: OKUserviceDB){
        serviceDao.addService(service)
    }
}