package com.example.share4care.shihan.roomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val status: String,
    @PrimaryKey val userName:String,
    val password: String,
    val name: String,
    val gender: String,
    val dob: String,
    val phone: String,
    val email: String,
    val address: String,
    val isOKU: String,
    val occupation: String,
    val companyName: String,
    val accountType: String,
    val imageLink: String
)
