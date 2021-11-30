package com.example.share4care.ee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.share4care.R
import com.example.share4care.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnPress.setOnClickListener(){
            it.findNavController().navigate(R.id.action_homeFragment2_to_calendarFragment2)
        }

        return binding.root

    }
}