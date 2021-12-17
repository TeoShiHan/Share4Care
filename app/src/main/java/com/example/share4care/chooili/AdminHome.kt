package com.example.share4care.chooili

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.share4care.R
import com.example.share4care.shihan.LoginActivity
import com.example.share4care.shihan.LoginActivity.Companion.USERNAME
import com.google.android.material.navigation.NavigationView

class AdminHome : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val username = intent.getStringExtra(USERNAME)
        findViewById<TextView>(R.id.tvAdminName).text = username

        val drawerLayout: DrawerLayout = findViewById(R.id.nav_drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        //val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<CardView>(R.id.cvVerifyUser).setOnClickListener() {
            val intent = Intent(this, Activity_Verify_User::class.java)
            startActivity(intent)
        }


        findViewById<CardView>(R.id.cvVerifyEST).setOnClickListener() {
            val intent = Intent(this, Activity_Verify_EST::class.java)
            startActivity(intent)
        }


        findViewById<CardView>(R.id.cvCreateEST).setOnClickListener() {
            val intent = Intent(this, CreateESTActivity::class.java)
            intent.putExtra(USERNAME, username)
            startActivity(intent)
        }


        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        fun onOptionsItemSelected(item: MenuItem): Boolean {

            if (toggle.onOptionsItemSelected(item)) {
                return true

            }
            return super.onOptionsItemSelected(item)
        }


    }

}