package com.example.share4care.shihan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.share4care.R
import com.example.share4care.databinding.FragmentRegContactInfoBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class RegContactInfo : Fragment() {

    private lateinit var binding: FragmentRegContactInfoBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?

    ): View? {

        val userViewModel: UserViewModel by activityViewModels()

        lateinit var contactData: UserContactInfo

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reg_contact_info, container, false)

        binding.regContactToPrevPageBtn.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_reg_contact_info_to_reg_personal_info)
        }

        binding.regContactToNextPageBtn.setOnClickListener(){
            var containError = actionValidatePhoneField(binding.regContactPhoneInput, binding.regContactPhoneContainer)
            if (containError){return@setOnClickListener}
            containError = actionValidateEmail(binding.regContactEmailInput, binding.regContactEmailContainer)
            if (containError){return@setOnClickListener}

            contactData = UserContactInfo(
                binding.regContactPhoneInput.text.toString(),
                binding.regContactEmailInput.text.toString()
            )

//            userViewModel.contactInformation = contactData
            userViewModel.pushPersonalInfoToFirebase()
            Navigation.findNavController(it).navigate(R.id.action_reg_contact_info_to_regCompanyInfo)
        }

        binding.toLoginFromRegContact.setOnClickListener {
            val goToLogin = getIntentToOtherActivity<LoginActivity>(activity as Activity)
            startActivity(goToLogin)
        }
        return binding.root
    }

    private fun actionValidatePhoneField(phoneField : TextInputEditText, phonefieldContainer: TextInputLayout): Boolean {
        val pattern =
            "^(01[(2-9|0)]\\d{7})\$|^(011\\d{8})\$|^(0\\d\\d{7})\$|^(0\\d{2}\\d{6})\$|^(0\\d\\d{8})\$".toRegex()
        val phoneFromUser = phoneField.text.toString()

        if(phoneFromUser == "") {
            phonefieldContainer.error = "Please enter your phone number"
            return true
        } else {
            phonefieldContainer.error=null
        }

        if(!pattern.matches(phoneFromUser)) {
            phonefieldContainer.error = "Please enter a valid phone number"
            return true
        } else {
            phonefieldContainer.error=null
            return false
        }
    }

    private fun actionValidateEmail(emailField : TextInputEditText, emailfieldContainer: TextInputLayout): Boolean{
        val pattern =
            "^[a-zA-Z]\\w+@(\\S+)\$".toRegex()
        val emailFromUser = emailField.text.toString()

        if(emailFromUser == "") {
            emailfieldContainer.error = "Please enter your email address"
            return true
        } else {
            emailfieldContainer.error=null
        }

        if(!pattern.matches(emailFromUser)) {
            emailfieldContainer.error = "Please enter a valid email number"
            return true
        } else {
            emailfieldContainer.error=null
            return false
        }

    }

    private inline fun <reified T : Any>getIntentToOtherActivity(currentActivity: Activity): Intent {
        return Intent(currentActivity, T::class.java)
    }
}