package com.example.share4care.contentData

import android.graphics.Bitmap
import java.io.Serializable

data class Event(
    var title:String,
    var host:String,
    var category:String,
    var description:String,
    var date:String,
    var address:String,
    var latitude:Double,
    var longtitude:Double,
    var contactNumber:String,
    var contactEmail:String,
    //var image:Bitmap
):Serializable
