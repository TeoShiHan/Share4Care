package com.example.share4care.shihan.roomData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.share4care.contentData.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventViewModel(application: Application): AndroidViewModel(application) {

    private val roomCaster = TypeCaster()
    val readAllData: LiveData<List<EventDB>>
    private val repository: EventRepository

    init {
        val eventDAO = EventDatabase.getDatabase(application).eventDAO()
        repository = EventRepository(eventDAO)
        readAllData = repository.readAllData
    }

    fun addEvent(event: EventDB){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEvent(event)
        }
    }

    fun iterateConvertAndAdd(eventList : MutableList<Event>){
        for(singleEvent in eventList){
            val tempEvent = roomCaster.getEventData(singleEvent)
            addEvent(tempEvent)
        }
    }

}

