package com.example.share4care.loginAndRegisterForm

import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase




class Teset {
}

fun main() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("message")

    myRef.setValue("Hello, World!")

    myRef.setValue("Hello, World!")
}