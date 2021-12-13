package com.example.share4care.chooili

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.share4care.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.nav_drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        toggle.isDrawerIndicatorEnabled=true
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

                R.id.admin_verifyUser-> Toast.makeText(
                    applicationContext,
                    "Clicked Verify User",
                    Toast.LENGTH_SHORT
                ).show()


                R.id.admin_verifyEST
                -> Toast.makeText(
                    applicationContext,
                    "Clicked Verify EST",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.admin_delAcc
                -> Toast.makeText(
                    applicationContext,
                    "Clicked Delete Account",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.admin_viewReport
                -> Toast.makeText(
                    applicationContext,
                    "Clicked View Report",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.admin_createEST
                -> Toast.makeText(
                    applicationContext,
                    "Clicked Create EST",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.admin_ESTdetails
                -> Toast.makeText(
                    applicationContext,
                    "Clicked EST details",
                    Toast.LENGTH_SHORT
                ).show()

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            //After type true, all error gone
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true

        }
        return super.onOptionsItemSelected(item)
    }
}