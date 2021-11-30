package com.example.share4care.ee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.share4care.R
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.share4care.databinding.FragmentPostDetailBinding

import com.google.android.material.tabs.TabLayout

class PostDetailFragment : Fragment() {

    private val tabLayout: TabLayout? = null
    private val firstViewPager: ViewPager? = null

    fun PostDetailFragment() {
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentPostDetailBinding.inflate(inflater, container, false)

//        val viewPager: ViewPager = binding.viewpagerContent
//        binding.tabs.setupWithViewPager(view)
//
//        val adapter = TabViewPagerAdapter(activity!!.supportFragmentManager)
//        adapter.addFragment(Tab1Fragment(), "Tab 1")
//        adapter.addFragment(Tab1Fragment(), "Tab 2")
//        adapter.addFragment(Tab1Fragment(), "Tab 3")
//        adapter.addFragment(Tab1Fragment(), "Tab 4")
//        viewPager.adapter = adapter

        return binding.root
    }

    companion object{
        val TAG = "PostDetailFragment"
    }
}