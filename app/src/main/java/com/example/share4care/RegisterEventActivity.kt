package com.example.share4care

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.example.share4care.contentData.Event
import com.example.share4care.databinding.ActivityRegisterEventBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.IOException
import java.util.*

class RegisterEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityRegisterEventBinding
    private var imgUri: Uri? = null
    val regNum:Regex = Regex("^(01[(2-9|0)]\\d{7})\$|^(011\\d{8})\$")
    val regMail:Regex = Regex("^[a-zA-Z]\\w+@(\\S+)\$")

    var imageData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imgUri  = data?.data
            binding.actualImage.setImageURI(data?.data)
        }
    }
    private lateinit var displayDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = resources.getStringArray(R.array.event_category)
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        binding.actualCategory.setAdapter(adapter)

        binding.imageHeader.setOnClickListener(){
            binding.actualImage.visibility= View.VISIBLE
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            imageData.launch(intent)
        }

        binding.actualContactNumber.doOnTextChanged { text, start, before, count ->
            if (!text.toString().matches(regNum)){
                binding.contactHeader.error="Invalid Phone Number"
            } else {
                binding.contactHeader.error = null
            }
        }

        binding.actualContactEmail.doOnTextChanged { text, start, before, count ->
            if (!text.toString().matches(regMail)){
                binding.emailHeader.error="Invalid Email Address"
            } else{
                binding.emailHeader.error=null
            }
        }
        displayDate = binding.actualDate
        displayDate.setOnClickListener(){
            materialDatePicker()
        }

        val doneBtn = binding.doneButton
        doneBtn.setOnClickListener(){
            val title = binding.actualTitle.text.toString()
            val host = binding.actualHost.text.toString()
            val category = (binding.actualCategory)?.text.toString()
            val description = binding.actualDescription.text.toString()
            val date = binding.actualDate.text.toString()
            val originalAddress = binding.actualAddress.text.toString()
            val gc = Geocoder(this, Locale.getDefault())
            val address = gc.getFromLocationName(originalAddress, 2)[0]
            val foundLatitude = address.latitude
            val foundLongtitude = address.longitude
            val contactNumber = binding.actualContactNumber.text.toString()
            val contactEmail = binding.actualContactEmail.text.toString()
            val image = uriToBitmap(imgUri!!)


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

    private fun materialDatePicker(){
        // Create the date picker builder and set the title
        val builder = MaterialDatePicker.Builder.datePicker()
            .also {
                title = "Select A Date"
            }
        // create the date picker
        val datePicker = builder.build()
        // set listener when date is selected
        datePicker.addOnPositiveButtonClickListener {
            // Create calendar object and set the date to be that returned from selection
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)
            val selectDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                    "${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
            displayDate.setText(selectDate)
        }
        datePicker.show(supportFragmentManager, datePicker.toString())
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var actualMonth = month+1
        var selectDate:String = "$dayOfMonth/$actualMonth/$year"
        displayDate.setText(selectDate)
    }

    private fun uriToBitmap(imgUri: Uri): Bitmap? {
        val bitmap: Bitmap
        return try {
            bitmap = if(Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, imgUri)
            } else {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imgUri))
            }
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun bitmapToDrawable(bitmap: Bitmap): BitmapDrawable {
        return BitmapDrawable(resources,bitmap)
    }

}