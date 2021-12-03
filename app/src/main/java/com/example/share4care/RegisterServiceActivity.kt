package com.example.share4care

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Service
import com.example.share4care.databinding.ActivityRegisterServiceBinding
import java.util.*

class RegisterServiceActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = resources.getStringArray(R.array.service_category)
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        binding.actualCategory.setAdapter(adapter)


        val doneBtn = binding.doneButton
        doneBtn.setOnClickListener() {
            val title = binding.actualTitle.text.toString()
            val host = binding.actualHost.text.toString()
            val category = (binding.actualCategory)?.text.toString()
            val description = binding.actualDescription.text.toString()
            val originalAddress = binding.actualAddress.text.toString()
            val gc = Geocoder(this, Locale.getDefault())
            val address = gc.getFromLocationName(originalAddress, 2)[0]
            val foundLatitude = address.latitude
            val foundLongtitude = address.longitude
            val contactNumber = binding.actualContactNumber.text.toString()
            val contactEmail = binding.actualContactEmail.text.toString()


            val newService = Service(
                title,
                host,
                category,
                description,
                originalAddress,
                foundLatitude,
                foundLongtitude,
                contactNumber,
                contactEmail
            )
            val data = Intent()
            data.putExtra(HomeActivity.SERVICE, newService)
            setResult(RESULT_OK, data)
            finish()
        }



    }



}