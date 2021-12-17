package com.example.share4care.contentData

import java.io.Serializable

data class PostDetails(
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
    var image: String,
    var status:Long,
    var likes: HashMap<*, *>,
    var dislikes: HashMap<*, *>,
    var save: HashMap<*, *>,
    var comment: MutableList<*>
):Serializable {
    constructor(title:String, host:String, category:String, description: String, date: String,
                address: String, latitude: Double, longtitude: Double,contactNumber: String, contactEmail: String, imgUrl:String ): this
        (title, host, category, description, date, address, latitude, longtitude, contactNumber, contactEmail, imgUrl, 0, hashMapOf<Any,Any>(), hashMapOf<Any,Any>(), hashMapOf<Any,Any>(), mutableListOf<String>())
}

