package com.example.share4care.shihan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.share4care.R
import com.example.share4care.databinding.FragmentRegCompanyInfoBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class RegCompanyInfo : Fragment() {

    private lateinit var binding: FragmentRegCompanyInfoBinding
    val args: RegCompanyInfoArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lateinit var companyData: UserCompanyInfo

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reg_company_info, container, false)

        binding.regCompanyToNextPageBtn.setOnClickListener(){
            var invalid : Boolean = actionValidateField(binding.companyNameInput, binding.regCompanyNameInputContainer, "Please enter a company name")
            if (invalid){return@setOnClickListener}
            invalid = actionValidateEmail(binding.regCompanyEmailInput, binding.regCompanyOccupationContainer)
            if (invalid){return@setOnClickListener}
            invalid = actionValidatePhoneField(binding.regCompanyTelInput, binding.regCompanyTelContainer)
            if (invalid){return@setOnClickListener}
            invalid = actionValidateAddressField(binding.regCompanyAddressInput, binding.regCompanyAddressContainer)
            if (invalid){return@setOnClickListener}
            invalid = actionValidateField(binding.regCompnayOccupationInput, binding.regCompanyOccupationContainer, "Please enter your occupation")
            if (invalid){return@setOnClickListener}
            invalid = actionValidateUrl(binding.regSocialMediaInput, binding.regSocialMediaContainer)
            if (invalid){return@setOnClickListener}

            companyData = UserCompanyInfo(
                binding.companyNameInput.text.toString(),
                binding.regCompanyEmailInput.text.toString(),
                binding.regCompanyTelInput.text.toString(),
                binding.regCompanyAddressInput.text.toString(),
                binding.regCompnayOccupationInput.text.toString(),
                binding.regSocialMediaInput.text.toString()
            )

            val action = RegCompanyInfoDirections.actionRegCompanyInfoToRegAccountInfo(companyData, args.contactData, args.personalData)
//          userViewModel.companyInformation = companyData
            Navigation.findNavController(it).navigate(action)
        }

        binding.fromRegCompanyToLogin.setOnClickListener(){
            val toLogin = getIntentToOtherActivity<RegAccountInfo>(activity as Activity)
            startActivity(toLogin)
        }

        binding.regIsPersonalUseCheckboc.setOnClickListener(){

            companyData = UserCompanyInfo(
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                "N/A"
            )
//          userViewModel.companyInformation = companyData
//          Navigation.findNavController(it).navigate(R.id.action_regCompanyInfo_to_regAccountInfo)
            val action = RegCompanyInfoDirections.actionRegCompanyInfoToRegAccountInfo(companyData, args.contactData, args.personalData)
            Navigation.findNavController(it).navigate(action)
        }

        return binding.root
    }

    private fun actionValidateField(field : TextInputEditText, fieldContainer: TextInputLayout, message: String): Boolean{
        if (field.text.toString() == ""){
            fieldContainer.error = message
            return true
        }
        else
            fieldContainer.error=null
        return false
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
            emailfieldContainer.error = "Please enter a email address"
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

    private fun actionValidateAddressField(addressField : EditText, addressFieldContainer: TextInputLayout): Boolean {

        if (addressField.text.toString() == ""){
            addressFieldContainer.error = "Please enter a address"
            return true
        }
        else{
            addressFieldContainer.error = null
        }

        val statesInMalaysia = arrayOf("Johor",
            "Kedah","Kelantan","Malacca","Melaka", "Negeri Sembilan","Pahang",
            "Perak","Perlis","Sabah","Sarawak","Selangor","Terengganu", "Kuala Lumpur",
            "Labuan", "Putrajaya")

        var contain_state : Boolean = false

        for(state in statesInMalaysia){
            if(addressField.text.toString().contains(state, ignoreCase = true)){
                contain_state = true
                break
            }
        }

        if(!contain_state){
            addressFieldContainer.error = "Please enter a valid address"
            return true
        }
        else{
            addressFieldContainer.error = null
        }

        val containPostCodePattern = ".*\\d{5}".toRegex()

        if (!containPostCodePattern.matches(addressField.text.toString())){
            addressFieldContainer.error = "Please enter a valid address"
            return true
        }

        return false
    }

    private fun actionValidateUrl(webField : EditText, webFieldContainer: TextInputLayout): Boolean {
        val pattern = "https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,}".toRegex()

        val webFromUser = webField.text.toString()

        if(webFromUser == "") {
            webFieldContainer.error = "Please enter a website"
            return true
        } else {
            webFieldContainer.error=null
        }

        if(!pattern.matches(webFromUser)) {
            webFieldContainer.error = "Please enter a valid website"
            return true
        } else {
            webFieldContainer.error=null
            return false
        }
    }

    private inline fun <reified T : Any>getIntentToOtherActivity(currentActivity: Activity): Intent {
        return Intent(currentActivity, T::class.java)
    }

//    private fun datAssemblingUser(){
//        val userTable = UserTableRecord(
//
//        )
//    }
//
//    private fun dataAssemblingCompnay(){
//
//    }
}