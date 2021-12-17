package com.example.share4care.contentData

import android.graphics.Bitmap
import java.io.Serializable

data class Service(
    var title:String,
    var host:String,
    var category:String,
    var description:String,
    var address:String,
    var latitude:Double,
    var longtitude:Double,
    var contactNumber:String,
    var contactEmail:String,
    var image: String,
    var status:Int,
    var like:  HashMap<*,*>?,
    var dislike:  HashMap<*,*>?,
    var save:  HashMap<*,*>?,
    var comment: MutableList<UserComment>?
):Serializable {
    constructor(title:String, host:String, category:String, description: String, address: String,
                latitude: Double, longtitude: Double,contactNumber: String, contactEmail: String, imgUrl:String ): this
        (title, host, category, description, address, latitude, longtitude, contactNumber, contactEmail, imgUrl, 0, hashMapOf<String,String>(), hashMapOf<String,String>(), hashMapOf<String,String>(), arrayListOf())
    }

