package com.example.share4care

import android.app.DatePickerDialog
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.example.share4care.contentData.Event
import com.example.share4care.databinding.ActivityRegisterEventBinding
import java.util.*

class RegisterEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityRegisterEventBinding
    private lateinit var displayDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categorySpinner = binding.actualCategory
        ArrayAdapter.createFromResource(this, R.array.event_category, android.R.layout.simple_spinner_item)
            .also{adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter=adapter
            }

        displayDate = binding.actualDate
        displayDate.setOnClickListener(){
            showDatePickerDialog()
        }

        val doneBtn = binding.doneButton
        doneBtn.setOnClickListener(){
            val title = binding.actualTitle.text.toString()
            val host = binding.actualHost.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val description = binding.actualDescription.text.toString()
            val date = binding.actualDate.text.toString()
            val originalAddress = binding.actualAddress.text.toString()
            val gc = Geocoder(this, Locale.getDefault())
            val address = gc.getFromLocationName(originalAddress, 2)[0]
            val foundLatitude = address.latitude
            val foundLongtitude = address.longitude
            val contactNumber = binding.actualContactNumber.text.toString()
            val contactEmail = binding.actualContactEmail.text.toString()


            val newEvent = Event(title, host, category, description, date, originalAddress, foundLatitude, foundLongtitude, contactNumber, contactEmail)
            val data = Intent()
            data.putExtra(HomeActivity.EVENT, newEvent)
            setResult(RESULT_OK, data)
            finish()
        }

    }

    private fun showDatePickerDialog(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, this, year, month, day )
        datePicker.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var actualMonth = month+1
        var selectDate:String = "$dayOfMonth/$actualMonth/$year"
        displayDate.setText(selectDate)
    }
}