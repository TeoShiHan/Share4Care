package com.example.share4care.shihan.roomData

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.share4care.contentData.UserComment

@Entity(tableName = "event_table")
class EventDB (
    @PrimaryKey val title:String,
    val host:String,
    val category:String,
    val description:String,
    val date:String,
    val address:String,
    val latitude:Double,
    val longtitude:Double,
    val contactNumber:String,
    val contactEmail:String,
    val image:String,
    val status:Int,
)