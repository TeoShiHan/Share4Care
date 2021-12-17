package com.example.share4care.chooili

import android.app.Activity
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
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.example.share4care.HomeActivity
import com.example.share4care.R
import com.example.share4care.contentData.Service
import com.example.share4care.databinding.ActivityAdminServiceBinding
import com.example.share4care.databinding.ActivityRegisterServiceBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.util.*

class AdminServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminServiceBinding
    var image: String = ""
    private var imgUri: Uri? = null
    private lateinit var imgView: ImageView

    val database = Firebase.database
    val myDatabaseRef = database.getReference("Services")
    var myStorageRef = FirebaseStorage.getInstance().getReference("images")

    val regNum: Regex =
        Regex("^(01[(2-9|0)]\\d{7})\$|^(011\\d{8})\$|^(0\\d\\d{7})\$|^(0\\d{2}\\d{6})\$|^(0\\d\\d{8})\$") //
    val regMail: Regex = Regex("^[a-zA-Z]\\w+@(\\S+)\$")

    var imageData =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                imgUri = data?.data
                Glide.with(imgView.context).load(imgUri).into(imgView)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imgView = findViewById(R.id.actualImage)

        val items = resources.getStringArray(R.array.service_category)
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        binding.actualCategory.setAdapter(adapter)

        binding.imageHeader.setOnClickListener() {
            binding.actualImage.visibility = View.VISIBLE
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imageData.launch(intent)
        }

        binding.actualContactNumber.doOnTextChanged { text, start, before, count ->
            if (!text.toString().matches(regNum)) {
                binding.contactHeader.error = "Invalid Phone Number"
            } else {
                binding.contactHeader.error = null
            }
        }

        binding.actualContactEmail.doOnTextChanged { text, start, before, count ->
            if (!text.toString().matches(regMail)) {
                binding.emailHeader.error = "Invalid Email Address"
            } else {
                binding.emailHeader.error = null
            }
        }

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
            //val image = uriToBitmap(imgUri!!)

            /*if(imgUri!=null){
                val fileRef = myStorageRef.child(title+host+category+"."+getFileExtension(imgUri!!))

                fileRef.putFile(imgUri!!)
                    .addOnSuccessListener { snapshot ->
                        val result = snapshot.metadata!!.reference!!.downloadUrl
                        result.addOnSuccessListener {
                            Toast.makeText(applicationContext, "Completed", Toast.LENGTH_LONG).show()
                            image = it.toString()
                        }
                    }
                    .addOnFailureListener{
                        Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                    }
            }*/

            if (imgUri != null) {
                val fileRef =
                    myStorageRef.child(title + host + category + "." + getFileExtension(imgUri!!))

                fileRef.putFile(imgUri!!)
                    .addOnSuccessListener { snapshot ->
                        val result = snapshot.storage.downloadUrl
                        result.addOnSuccessListener {
                            Toast.makeText(applicationContext, "Completed", Toast.LENGTH_LONG)
                                .show()
                            image = it.toString()

                            val newService = Service(
                                title,
                                host,
                                category,
                                description,
                                originalAddress,
                                foundLatitude,
                                foundLongtitude,
                                contactNumber,
                                contactEmail,
                                image,1,
                                hashMapOf<String,String>(),
                                hashMapOf<String,String>(),
                                hashMapOf<String,String>(),
                                arrayListOf()

                            )

                            val key = title

                            myDatabaseRef.child(key).setValue(newService)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Service added",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        applicationContext,
                                        it.message.toString(),
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            finish()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                    }
            }


        }
    }
//"now < 1641830400000"
    /*fun uriToBitmap(selectedFileUri: Uri) {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }*/
    /*private fun assetsToBitmap(fileName:String): Bitmap?{
        return try{
            val stream = assets.open(fileName)
            BitmapFactory.decodeStream(stream)
        }catch (e: IOException){
            e.printStackTrace()
            null
        }
    }*/

    private fun getFileExtension(uri: Uri): String? {
        val mime = MimeTypeMap.getSingleton()
        val cr = contentResolver
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }

    private fun uriToBitmap(imgUri: Uri): Bitmap? {
        val bitmap: Bitmap
        return try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
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
        return BitmapDrawable(resources, bitmap)
    }
}