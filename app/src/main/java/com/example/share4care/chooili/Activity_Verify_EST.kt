package com.example.share4care.chooili

import android.content.Intent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodSession
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.share4care.R
import com.example.share4care.chooili.adapter.EST_Adapter
import com.example.share4care.chooili.model.EST
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Activity_Verify_EST : AppCompatActivity(),  EST_Adapter.OnItemClickListener {

    private lateinit var unverifyEvent : List <EST>
    private lateinit var unverifyService : List <EST>
    private lateinit var unverifyTravel : List <EST>
    private var unverifylist = mutableListOf <EST>()

    val database = Firebase.database
    val myEventRef = database.getReference("Events")
    val myServiceRef= database.getReference("Services")
    val myTravelRef = database.getReference("Travels")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_est)
        val estRV: RecyclerView = findViewById(R.id.estRecyclerView)
        val drawerLayout: DrawerLayout = findViewById(R.id.nav_drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.admin_home -> Toast.makeText(
                    applicationContext,
                    "Clicked Home",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.admin_verifyUser
                -> startActivity(Intent(this, Activity_Verify_User::class.java))

                R.id.admin_verifyEST
                -> startActivity(Intent(this, Activity_Verify_EST::class.java))

                R.id.admin_createEST
                -> startActivity(Intent(this, CreateESTActivity::class.java))


            }
            drawerLayout.closeDrawer(GravityCompat.START)
            //After type true, all error gone
            true
        }

        loadEvent(object:EventCallback{
            override fun onEventBack(s: MutableList<EST>) {
                //unverifyEvent = s
                unverifylist.addAll(s.toList())
                estRV.adapter =  EST_Adapter(unverifylist,this@Activity_Verify_EST)
                estRV.layoutManager = LinearLayoutManager(application)
                estRV.setHasFixedSize(true)


            }
        },0)

        loadService(object:ServiceCallback{
            override fun onServiceBack(s: MutableList<EST>) {
                //unverifyService= s
                unverifylist.addAll(s.toList())

            }
        },0)

        loadTravel(object:TravelCallback{
            override fun onTravelBack(s: MutableList<EST>) {
                unverifylist.addAll(s.toList())
                //unverifylist.addAll(unverifyEvent)
                //unverifylist.addAll(unverifyService)
                //unverifylist.addAll(unverifyTravel)


            }
        },0)

    }

    override fun onItemClick(position: Int) {
        Toast.makeText(applicationContext, position.toString(),Toast.LENGTH_LONG).show()
    }

    private fun loadEvent(callback:EventCallback, key: Int) {
        val ref = myEventRef
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val list = mutableListOf<EST>()
                if (p0.exists()) {
                    for (c in p0.children) {
                        if ((c.child("status").value as Long).toInt() == key) {
                            val title = c.child("title").value.toString()
                            val host = c.child("host").value.toString()
                            val category = c.child("category").value.toString()
                            val description = c.child("description").value.toString()
                            val date = c.child("date").value.toString()
                            val address = c.child("address").value.toString()
                            val contactNumber =
                                c.child("contactNumber").value.toString()
                            val contactEmail =
                                c.child("contactEmail").value.toString()
                            val status =
                                (c.child("status").value as Long).toInt()

                            list.add(
                                EST(
                                    title,
                                    host,
                                    description,
                                    date,
                                    contactNumber,
                                    contactEmail,
                                    category,
                                    address,
                                    status
                                )
                            )
                        }
                    }
                    callback.onEventBack(list)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        ref.addValueEventListener(refListener)
    }

    private fun loadService(callback:ServiceCallback, key: Int) {
        val ref = myServiceRef
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val list = mutableListOf<EST>()
                if (p0.exists()) {
                    for (c in p0.children) {
                        if ((c.child("status").value as Long).toInt() == key) {
                            val title = c.child("title").value.toString()
                            val host = c.child("host").value.toString()
                            val category = c.child("category").value.toString()
                            val description = c.child("description").value.toString()
                            val date = c.child("date").value.toString()
                            val address = c.child("address").value.toString()
                            val contactNumber =
                                c.child("contactNumber").value.toString()
                            val contactEmail =
                                c.child("contactEmail").value.toString()
                            val status =
                                (c.child("status").value as Long).toInt()

                            list.add(
                                EST(
                                    title,
                                    host,
                                    description,
                                    "",
                                    contactNumber,
                                    contactEmail,
                                    category,
                                    address,
                                    status
                                )
                            )
                        }
                    }
                    callback.onServiceBack(list)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        ref.addValueEventListener(refListener)
    }

    private fun loadTravel(callback:TravelCallback, key: Int) {
        val ref = myTravelRef
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val list = mutableListOf<EST>()
                if (p0.exists()) {
                    for (c in p0.children) {
                        if ((c.child("status").value as Long).toInt() == key) {
                            val title = c.child("title").value.toString()
                            val host = c.child("host").value.toString()
                            val category = c.child("category").value.toString()
                            val description = c.child("description").value.toString()
                            val date = c.child("date").value.toString()
                            val address = c.child("address").value.toString()
                            val contactNumber =
                                c.child("contactNumber").value.toString()
                            val contactEmail =
                                c.child("contactEmail").value.toString()
                            val status =
                                (c.child("status").value as Long).toInt()

                            list.add(
                                EST(
                                    title,
                                    host,
                                    description,
                                    "",
                                    contactNumber,
                                    contactEmail,
                                    category,
                                    address,
                                    status
                                )
                            )
                        }
                    }
                    callback.onTravelBack(list)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        ref.addValueEventListener(refListener)
    }


    interface EventCallback {
        fun onEventBack(s: MutableList<EST>)
    }

    interface ServiceCallback {
        fun onServiceBack(s: MutableList<EST>)
    }

    interface TravelCallback {
        fun onTravelBack(s: MutableList<EST>)
    }

}