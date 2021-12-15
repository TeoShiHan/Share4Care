package com.example.share4care

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.share4care.contentData.Event
import com.example.share4care.databinding.ActivityEstBinding
import com.example.share4care.ee.PostCommentsFragment
import com.example.share4care.ee.PostDetailsFragment
import com.example.share4care.ee.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ESTActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEstBinding
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var postAdapter: ViewPagerAdapter
    private lateinit var eventObj : Event
    private lateinit var detailBundle: Bundle
    private lateinit var commentBundle: Bundle
    private lateinit var postDetailsFragment: PostDetailsFragment
    private lateinit var postCommentsFragment: PostCommentsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_est)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_est)

        tabLayout = binding.tabs
        viewPager2 = binding.viewpagerContent

        eventObj = intent.getSerializableExtra("eventObj") as Event

        detailBundle = Bundle()
        detailBundle.putString("title",eventObj.title)
        detailBundle.putString("address",eventObj.address)
        detailBundle.putString("category",eventObj.category)
        detailBundle.putString("contactEmail",eventObj.contactEmail)
        detailBundle.putString("contactNumber",eventObj.contactNumber)
        detailBundle.putString("date",eventObj.date)
        detailBundle.putString("description",eventObj.description)
        detailBundle.putString("host",eventObj.host)
        detailBundle.putString("image",eventObj.image)
        detailBundle.putInt("like",eventObj.like!!.size)
        detailBundle.putInt("dislike",eventObj.dislike!!.size)

//        commentBundle = Bundle()
//        detailBundle.putSerializable("eventObj",eventObj)
        binding.tvTitle.text = eventObj.title
        Glide.with(binding.imageView.context).load(eventObj.image).into(binding.imageView)

        postAdapter = ViewPagerAdapter(supportFragmentManager,lifecycle)
        postAdapter.setArguments(detailBundle)

        viewPager2.adapter = postAdapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}