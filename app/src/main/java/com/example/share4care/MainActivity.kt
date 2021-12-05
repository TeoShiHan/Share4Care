package com.example.share4care

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.share4care.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.template.androidtemplate.ui.main.RecyclerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navController = Navigation.findNavController(this, R.id.container)
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        val bottomSheetParent = findViewById<ConstraintLayout>(R.id.bottom_sheet_parent)
        val mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetParent)
        mBottomSheetBehaviour.apply {
            peekHeight=100
            this.state = BottomSheetBehavior.STATE_SETTLING
            isFitToContents = false
            halfExpandedRatio = 0.45f
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        recyclerAdapter = RecyclerAdapter()
        binding.recyclerView.adapter = recyclerAdapter
    }
}