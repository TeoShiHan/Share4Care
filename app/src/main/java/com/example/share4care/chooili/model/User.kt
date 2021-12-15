package com.example.share4care.chooili.model

import java.io.Serializable

data class User(
    var accType:String,
    var userAddress:String,
    var userDOB:String,
    var userEmail:String,
    var userGender:String,
    var userIsOKU:String,
    var Name:String,
    var occupation:String,
    var password: String,
    var userPhone:String,
    var userName:String,
):Serializable

