package com.example.share4care.chooili

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.share4care.R
import com.example.share4care.chooili.adapter.User_Adapter
import com.example.share4care.chooili.model.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Activity_Verify_User : AppCompatActivity(), User_Adapter.OnItemClickListener {

    private var user = mutableListOf<User>()

    val database = Firebase.database
    val userRef = database.getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_user)
        val userRV: RecyclerView = findViewById(R.id.userRecyclerView)

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
        loadUser(object: userCallback {
            override fun onUserBack(s: MutableList<User>) {
                user =s
                userRV.adapter =  User_Adapter(user,this@Activity_Verify_User)
                userRV.layoutManager = LinearLayoutManager(application)
                userRV.setHasFixedSize(true)

            }
        },"0")


    }

    override fun onItemClick(position: Int) {
        val selectedUser = user[position]
        val intent = Intent(this, Activity_About_Us::class.java)
        intent.putExtra("USER", selectedUser)
        startActivity(intent)
        Toast.makeText(applicationContext, position.toString(), Toast.LENGTH_LONG).show()
    }

    private fun loadUser(callback:userCallback, key: String) {
        val ref = userRef
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val list = mutableListOf<User>()
                if (p0.exists()) {
                    for (c in p0.children) {
                        if ((c.child("status").value).toString() == key) {
                            val accType = c.child("accountType").value.toString()
                            val address = c.child("address").value.toString()
                            val dob = c.child("dob").value.toString()
                            val email = c.child("email").value.toString()
                            val gender = c.child("gender").value.toString()
                            val isOKU = c.child("isOKU").value.toString()
                            val name = c.child("userName").value.toString()
                            val occupation = c.child("occupation").value.toString()
                            val password = c.child("password").value.toString()
                            val phone = c.child("phone").value.toString()
                            val userName = c.child("userName").value.toString()

                            list.add(
                                User(
                                    accType,
                                    address,
                                    dob,
                                    email,
                                    gender,
                                    isOKU,
                                    name,
                                    occupation,
                                    password,
                                    phone,
                                    userName,
                                )
                            )
                        }
                    }
                    callback.onUserBack(list)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        ref.addValueEventListener(refListener)
    }
    interface userCallback {
        fun onUserBack(s: MutableList<User>)}
}