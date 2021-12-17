package com.example.share4care.shihan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.share4care.R
import com.example.share4care.databinding.FragmentRegContactInfoBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage


class RegContactInfo : Fragment() {

    private lateinit var binding: FragmentRegContactInfoBinding
    private val args: RegContactInfoArgs by navArgs()
    private var contactData = UserContactInfo("", "", "")
    private var globalURI: Uri? = null

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?

    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reg_contact_info, container, false)

        binding.regContactToPrevPageBtn.setOnClickListener() {
            Navigation.findNavController(it)
                .navigate(R.id.action_reg_contact_info_to_reg_personal_info)
        }


        binding.regContactToNextPageBtn.setOnClickListener() {
            var containError = actionValidatePhoneField(
                binding.regContactPhoneInput,
                binding.regContactPhoneContainer
            )
            if (containError) {
                return@setOnClickListener
            }
            containError =
                actionValidateEmail(binding.regContactEmailInput, binding.regContactEmailContainer)
            if (containError) {
                return@setOnClickListener
            }
            if(globalURI == null){return@setOnClickListener}

            contactData.phoneNumber = binding.regContactPhoneInput.text.toString()
            contactData.email = binding.regContactEmailInput.text.toString()
            pushImageAndSaveLink(globalURI!!)

            val action =
                RegContactInfoDirections.actionRegContactInfoToRegCompanyInfo(
                    contactData,
                    args.personalInfo
                )

            Navigation.findNavController(it).navigate(action)
//            userViewModel.contactInformation = contactData
//            Navigation.findNavController(it)
//                .navigate(R.id.action_reg_contact_info_to_regCompanyInfo)
        }


        binding.emptyProfileImage.setOnClickListener() {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Title"), 1)
        }


        binding.toLoginFromRegContact.setOnClickListener {
            val goToLogin = getIntentToOtherActivity<LoginActivity>(activity as Activity)
            startActivity(goToLogin)
        }
        return binding.root
    }


    private fun actionValidatePhoneField(
        phoneField: TextInputEditText,
        phonefieldContainer: TextInputLayout

    ): Boolean {
        val pattern =
            "^(01[(2-9|0)]\\d{7})\$|^(011\\d{8})\$|^(0\\d\\d{7})\$|^(0\\d{2}\\d{6})\$|^(0\\d\\d{8})\$".toRegex()
        val phoneFromUser = phoneField.text.toString()

        if (phoneFromUser == "") {
            phonefieldContainer.error = "Please enter your phone number"
            return true
        } else {
            phonefieldContainer.error = null
        }

        if (!pattern.matches(phoneFromUser)) {
            phonefieldContainer.error = "Please enter a valid phone number"
            return true
        } else {
            phonefieldContainer.error = null
            return false
        }
    }


    private fun actionValidateEmail(
        emailField: TextInputEditText,
        emailfieldContainer: TextInputLayout
    ): Boolean {
        val pattern =
            "^[a-zA-Z]\\w+@(\\S+)\$".toRegex()
        val emailFromUser = emailField.text.toString()

        if (emailFromUser == "") {
            emailfieldContainer.error = "Please enter your email address"
            return true
        } else {
            emailfieldContainer.error = null
        }

        if (!pattern.matches(emailFromUser)) {
            emailfieldContainer.error = "Please enter a valid email number"
            return true
        } else {
            emailfieldContainer.error = null
            return false
        }
    }

    private inline fun <reified T : Any> getIntentToOtherActivity(currentActivity: Activity): Intent {
        return Intent(currentActivity, T::class.java)
    }

    private fun getFileExtension(uri: Uri): String? {
        val mime = MimeTypeMap.getSingleton()
        val cr = activity?.contentResolver
        if (cr != null) {
            return mime.getExtensionFromMimeType(cr.getType(uri))
        }
        return ""
    }

    private fun pushImageAndSaveLink(uri: Uri) {
        val myStorageRef = FirebaseStorage.getInstance().getReference("images")
        val fileRef =
            myStorageRef.child(args.personalInfo.name.toString() + "." + getFileExtension(uri!!))
        fileRef.putFile(uri)
            .addOnSuccessListener { snapshot ->
                val result = snapshot.storage.downloadUrl
                result.addOnSuccessListener {
                    contactData.imglink = it.toString()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val uri = data?.data
            binding.emptyProfileImage.setImageURI(uri)
            globalURI = uri!!
        }
    }
}