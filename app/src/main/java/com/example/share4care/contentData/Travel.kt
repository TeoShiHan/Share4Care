package com.example.share4care.contentData

import android.graphics.Bitmap
import java.io.Serializable

data class Travel(
    var title:String,
    var host:String,
    var category:String,
    var description:String,
    var address:String,
    var latitude:Double,
    var longtitude:Double,
    var contactNumber:String,
    var contactEmail:String,
    var image: Bitmap?
):Serializable {
    constructor(title:String, host:String, category:String, description: String, address: String, latitude: Double, longtitude: Double,contactNumber: String, contactEmail: String ):
            this(title, host, category, description, address, latitude, longtitude, contactNumber, contactEmail, null
            )
}

