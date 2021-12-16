package com.example.share4care

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.share4care.databinding.ActivityHomeBinding
import com.google.android.gms.maps.model.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.share4care.ee.CalendarFragment
import com.example.share4care.ee.HomeFragment
import com.example.share4care.ee.PostFragment
import com.example.share4care.ee.ProfileFragment
import com.google.android.gms.location.*


class HomeActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeFragment = HomeFragment()
        calendarFragment = CalendarFragment()
        profileFragment = ProfileFragment()

        openFragment(homeFragment)

        binding.bottomNavigation.setOnItemSelectedListener OnItemSelectedListener@{
                item ->
            when(item.itemId){
                R.id.home_navigation -> {
                    openFragment(homeFragment)
                }
                R.id.post_navigation -> {
                    openFragment(calendarFragment)
                }
                R.id.profile_navigation -> {
                    openFragment(profileFragment)
                }
                else -> {
                    openFragment(homeFragment)
                }
            }

            return@OnItemSelectedListener true
        }

    }

    private fun openFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit();
    }

    companion object{
        const val EVENT = "com.example.share4care.EVENT"
        const val SERVICE = "com.example.share4care.SERVICE"
        const val TRAVEL = "com.example.share4care.TRAVEL"
    }

}