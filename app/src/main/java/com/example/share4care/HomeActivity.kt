package com.example.share4care

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.share4care.databinding.ActivityHomeBinding
import com.google.android.gms.maps.model.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.share4care.ee.CalendarFragment
import com.example.share4care.ee.HomeFragment
import com.example.share4care.ee.PostFragment
import com.example.share4care.ee.ProfileFragment
import com.example.share4care.shihan.LoginActivity.Companion.USERNAME
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

        val username = intent.getStringExtra(USERNAME)
        homeFragment = HomeFragment()
        calendarFragment = CalendarFragment()
        profileFragment = ProfileFragment()

        openFragment(homeFragment, null)

        binding.bottomNavigation.setOnItemSelectedListener OnItemSelectedListener@{
                item ->
            when(item.itemId){
                R.id.home_navigation -> {
                    openFragment(homeFragment, null)
                }
                R.id.post_navigation -> {
                    openFragment(calendarFragment, null)
                }
                R.id.profile_navigation -> {
                    val bundle = bundleOf(Pair("username", username))
                    openFragment(profileFragment,bundle)
                }
                else -> {
                    openFragment(homeFragment, null)
                }
            }

            return@OnItemSelectedListener true
        }

    }

    private fun openFragment(fragment: Fragment, bundle: Bundle?){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit();
    }

    companion object{
        const val EVENT = "com.example.share4care.EVENT"
        const val SERVICE = "com.example.share4care.SERVICE"
        const val TRAVEL = "com.example.share4care.TRAVEL"
    }

}