package com.example.share4care.ee

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private lateinit var bundle : Bundle

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if (position==1){
            val postCommentsFragment = PostCommentsFragment()
            postCommentsFragment.arguments = bundle
            return postCommentsFragment
        }
        val postDetailsFragment = PostDetailsFragment()
        postDetailsFragment.arguments = bundle
        return postDetailsFragment
    }

    fun setArguments(b : Bundle){
        bundle = b
    }
}