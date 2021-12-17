package com.example.share4care.shihan.roomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_table")
data class OKUserviceDB (
    @PrimaryKey var title:String,
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
)