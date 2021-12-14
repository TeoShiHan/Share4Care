package com.example.share4care.loginAndRegisterForm

import android.util.Log
import com.google.firebase.database.FirebaseDatabase


class Firebase{
    val database = FirebaseDatabase.getInstance()

    private inline fun <reified T : Any>write_object_to_firebase(topic: String, obj: T){
        FirebaseDatabase.getInstance().getReference(topic).setValue(obj)
    }

    fun read_data_as_list_of_object(topic: String){
        var mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(topic).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }
}



