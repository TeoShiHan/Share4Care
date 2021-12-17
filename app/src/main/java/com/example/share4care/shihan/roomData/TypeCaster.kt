package com.example.share4care.shihan.roomData

import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Service
import com.example.share4care.contentData.Travel

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
            null,
            null,
            null,
            null
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
            null,
            null,
            null,
            null
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
            null,
            null,
            null,
            null
        )
    }

    fun convertTravelDataListToRuntimeList(requiredConvertedList: List<TravelDB>): List<Travel>? {
        val convertedList: List<Travel>? = null
        for (singleItem in requiredConvertedList){
            val tempRecord = convertTravelDataToRuntime(singleItem)
            convertedList?.toMutableList()?.add(tempRecord)
        }
        return convertedList
    }


    fun convertServiceDataListToRuntimeList(requiredConvertedList: List<OKUserviceDB>): List<Service>? {
        val convertedList: List<Service>? = null
        for (singleItem in requiredConvertedList){
            val tempRecord = convertServiceDataToRuntimeSupport(singleItem)
            convertedList?.toMutableList()?.add(tempRecord)
        }
        return convertedList
    }


    fun convertEventDataListToRuntimeList(requiredConvertedList: List<EventDB>): List<Event>? {
        val convertedList: List<Event>? = null
        for (singleItem in requiredConvertedList){
            val tempRecord = convertEventDBDataToRuntimeSupport(singleItem)
            convertedList?.toMutableList()?.add(tempRecord)
        }
        return convertedList
    }
}