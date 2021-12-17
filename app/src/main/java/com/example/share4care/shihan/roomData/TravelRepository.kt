package com.example.share4care.shihan.roomData

import androidx.lifecycle.LiveData

class TravelRepository(private val travelDao: TravelDao) {
    val readAllData: LiveData<List<TravelDB>> = travelDao.readAllData()

    suspend fun addTravel(travel: TravelDB){
        travelDao.addTravel(travel)
    }
}