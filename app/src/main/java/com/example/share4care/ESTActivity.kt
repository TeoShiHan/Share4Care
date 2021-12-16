package com.example.share4care

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Service
import com.example.share4care.contentData.Travel
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
    private lateinit var estObj : Any
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

        var title = ""
        var address = ""
        var category = ""
        var contactEmail = ""
        var contactNumber = ""
        var date = ""
        var description = ""
        var host = ""
        var image = ""
        var like = arrayListOf<String>()
        var dislike = arrayListOf<String>()

        estObj = when(intent.getSerializableExtra("eventObj")){
            is Event -> {
                val obj = intent.getSerializableExtra("eventObj") as Event
                title = obj.title
                address = obj.address
                category = obj.category
                contactEmail = obj.contactEmail
                contactNumber = obj.contactNumber
                date = obj.date
                description = obj.description
                host = obj.host
                image = obj.image
                like = obj.like as ArrayList<String>
                dislike = obj.dislike as ArrayList<String>
            }
            is Service -> {
                val obj = intent.getSerializableExtra("eventObj") as Service
                title = obj.title
                address = obj.address
                category = obj.category
                contactEmail = obj.contactEmail
                contactNumber = obj.contactNumber
                description = obj.description
                host = obj.host
                image = obj.image
                like = obj.like as ArrayList<String>
                dislike = obj.dislike as ArrayList<String>
            }
            is Travel -> {
                val obj = intent.getSerializableExtra("eventObj") as Travel
                title = obj.title
                address = obj.address
                category = obj.category
                contactEmail = obj.contactEmail
                contactNumber = obj.contactNumber
                description = obj.description
                host = obj.host
                image = obj.image
                like = obj.like as ArrayList<String>
                dislike = obj.dislike as ArrayList<String>
            }
            else -> intent.getSerializableExtra("eventObj") as Event
        }

        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        detailBundle = Bundle()
        detailBundle.putString("title",title)
        detailBundle.putString("address",address)
        detailBundle.putString("category",category)
        detailBundle.putString("contactEmail",contactEmail)
        detailBundle.putString("contactNumber",contactNumber)
        detailBundle.putString("date",date)
        detailBundle.putString("description",description)
        detailBundle.putString("host",host)
        detailBundle.putString("image",image)
        detailBundle.putInt("like",like!!.size)
        detailBundle.putInt("dislike",dislike!!.size)

//        commentBundle = Bundle()
//        detailBundle.putSerializable("eventObj",eventObj)
        Glide.with(binding.imageView.context).load(image).into(binding.imageView)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}