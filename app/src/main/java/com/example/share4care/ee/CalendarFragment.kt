package com.example.share4care.ee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.share4care.R
import com.example.share4care.databinding.FragmentCalendarBinding
import com.google.android.material.datepicker.MaterialDatePicker

class CalendarFragment : Fragment() {

    private lateinit var binding : FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding.dateFromInputLayout.setEndIconOnClickListener{
            val dateRangePicker = MaterialDatePicker.Builder
                                    .dateRangePicker()
                                    .setTitleText("Select a date range")
                                    .build()
            dateRangePicker.showsDialog
        }

        return binding.root
    }
}