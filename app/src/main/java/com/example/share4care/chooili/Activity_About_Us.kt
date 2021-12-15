package com.example.share4care.chooili

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.share4care.R
import com.example.share4care.chooili.model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Activity_About_Us : AppCompatActivity() {

    val database = Firebase.database
    val userRef = database.getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        //val name = intent.getStringExtra("userName")
        //val phone = intent.getStringExtra("userPhone")
        val user = intent.getSerializableExtra("USER") as User

        findViewById<TextView>(R.id.tvName).text = user.userName
        findViewById<TextView>(R.id.tvBirthday).text = user.userDOB
        findViewById<TextView>(R.id.tvAddress).text = user.userAddress
        findViewById<TextView>(R.id.tvPhone).text = user.userPhone
        findViewById<TextView>(R.id.tvEmail).text = user.userEmail
        findViewById<TextView>(R.id.tvOku).text = user.userIsOKU

        val btnVerify : Button =  findViewById<Button>(R.id.btnVerify)
        val btnDelete : Button =  findViewById<Button>(R.id.btnDelete)



        btnVerify.setOnClickListener {
            val verify = HashMap<String, Any>()
            verify.put("status", "1")
            val key1 = user.userName
            userRef.child(key1).updateChildren(verify)

        }
        btnDelete.setOnClickListener {
            val key1 = user.userName
            userRef.child(key1).removeValue()

        }

    }
}