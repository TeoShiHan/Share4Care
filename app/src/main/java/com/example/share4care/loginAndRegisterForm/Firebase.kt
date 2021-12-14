package com.example.share4care.loginAndRegisterForm

import android.util.Log
import com.example.share4care.ee.PostDetailFragment.Companion.TAG
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener








class Firebase{
    val database = FirebaseDatabase.getInstance()

    fun read_data_as_list_of_object(topic: String){
        var mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(topic).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }
}



