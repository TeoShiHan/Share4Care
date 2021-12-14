package com.example.share4care.loginAndRegisterForm

import java.util.*

data class UserPersonalInfo(
    val primaryKey: UUID,
    val name: String,
    val gender: String,
    val DOB:String,
    val address:String,
    val isOKU:String)


