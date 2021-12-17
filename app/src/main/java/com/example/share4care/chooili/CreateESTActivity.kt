package com.example.share4care.chooili

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.share4care.R
import com.example.share4care.RegisterEventActivity
import com.example.share4care.RegisterServiceActivity
import com.example.share4care.RegisterTravelActivity

class CreateESTActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_estactivity)

        val btnEvent : Button =findViewById(R.id.btnCreateEvent)
        val btnService : Button =findViewById(R.id.btnCreateService)
        val btnTravel : Button = findViewById(R.id.btnCreateTravel)


        btnEvent.setOnClickListener(){
            val intent = Intent(this, AdminEventActivity::class.java)
            startActivity(intent)
        }

        btnService.setOnClickListener(){
            val intent = Intent(this, AdminServiceActivity::class.java)
            startActivity(intent)
        }

        btnTravel.setOnClickListener(){
            val intent = Intent(this, AdminTravelActivity::class.java)
            startActivity(intent)
        }


    }
}