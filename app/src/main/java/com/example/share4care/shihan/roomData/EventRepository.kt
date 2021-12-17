package com.example.share4care.shihan.roomData

import androidx.lifecycle.LiveData

class EventRepository(private val eventDAO: EventDAO) {
    val readAllData: LiveData<List<EventDB>> = eventDAO.readAllData()
    suspend fun addEvent(eventDB: EventDB){
        eventDAO.addEvent(eventDB)
    }
}