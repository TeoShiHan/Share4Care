package com.example.share4care.shihan.roomData

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Service
import com.example.share4care.contentData.Travel

class TypeCaster {
    fun getEventData(event: Event):EventDB{
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
    fun getTravelData(travel: Travel):TravelDB{
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
    fun getServiceData(service: Service):OKUserviceDB{
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

}