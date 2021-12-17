package com.example.share4care.shihan.roomData

import android.app.Activity
import android.widget.Toast
import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Service
import com.example.share4care.contentData.Travel
import com.example.share4care.contentData.UserComment

class TypeCaster {
    fun getEventData(event: Event): EventDB {
        return EventDB(
            event.title,
            event.host,
            event.category,
            event.description,
            event.date,
            event.address,
            event.latitude,
            event.longtitude,
            event.contactNumber,
            event.contactEmail,
            event.image,
            event.status
        )
    }

    fun getTravelData(travel: Travel): TravelDB {
        return TravelDB(
            travel.title,
            travel.host,
            travel.category,
            travel.description,
            travel.address,
            travel.latitude,
            travel.longtitude,
            travel.contactNumber,
            travel.contactEmail,
            travel.image,
            travel.status
        )
    }

    fun getServiceData(service: Service): OKUserviceDB {
        return OKUserviceDB(
            service.title,
            service.host,
            service.category,
            service.description,
            service.address,
            service.latitude,
            service.longtitude,
            service.contactNumber,
            service.contactEmail,
            service.image,
            service.status
        )
    }

    fun convertEventDBDataToRuntimeSupport(event: EventDB): Event {
        return Event(
            event.title,
            event.host,
            event.category,
            event.description,
            event.date,
            event.address,
            event.latitude,
            event.longtitude,
            event.contactNumber,
            event.contactEmail,
            event.image,
            event.status,
            hashMapOf<String,String>(),
            hashMapOf<String,String>(),
            hashMapOf<String,String>(),
            mutableListOf<UserComment>(),
        )
    }

    fun convertServiceDataToRuntimeSupport(service: OKUserviceDB): Service {
        return Service(
            service.title,
            service.host,
            service.category,
            service.description,
            service.address,
            service.latitude,
            service.longtitude,
            service.contactNumber,
            service.contactEmail,
            service.image,
            service.status,
            hashMapOf<String,String>(),
            hashMapOf<String,String>(),
            hashMapOf<String,String>(),
            mutableListOf<UserComment>(),
        )
    }

    fun convertTravelDataToRuntime(travel: TravelDB): Travel {
        return Travel(
            travel.title,
            travel.host,
            travel.category,
            travel.description,
            travel.address,
            travel.latitude,
            travel.longtitude,
            travel.contactNumber,
            travel.contactEmail,
            travel.image,
            travel.status,
            hashMapOf<String,String>(),
            hashMapOf<String,String>(),
            hashMapOf<String,String>(),
            mutableListOf<UserComment>(),
        )
    }

    fun convertTravelDataListToRuntimeList(requiredConvertedList: List<TravelDB>): MutableList<Travel>? {
        val convertedList = mutableListOf<Travel>()
        for (singleItem in requiredConvertedList){
            val tempRecord = convertTravelDataToRuntime(singleItem)
            convertedList?.add(tempRecord)
        }
        return convertedList
    }


    fun convertServiceDataListToRuntimeList(requiredConvertedList: List<OKUserviceDB>): MutableList<Service>? {
        val convertedList = mutableListOf<Service>()
        for (singleItem in requiredConvertedList){
            val tempRecord = convertServiceDataToRuntimeSupport(singleItem)
            convertedList?.add(tempRecord)
        }
        return convertedList
    }


    fun convertEventDataListToRuntimeList(requiredConvertedList: List<EventDB>): MutableList<Event>? {
        val convertedList = mutableListOf<Event>()
        for (singleItem in requiredConvertedList){
            val tempRecord = convertEventDBDataToRuntimeSupport(singleItem)
            convertedList?.add(tempRecord)
        }
        return convertedList
    }
}