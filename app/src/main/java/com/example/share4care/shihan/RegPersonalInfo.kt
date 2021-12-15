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
import com.example.share4care.R
import com.example.share4care.databinding.FragmentRegPersonalInfoBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*


class RegPersonalInfo : Fragment() {

    private lateinit var binding: FragmentRegPersonalInfoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Variables
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reg_personal_info, container, false)

        val view = inflater.inflate(R.layout.fragment_reg_personal_info, container, false)

        val datePicker = getDatePicker()

        val fragmentManager = activity?.supportFragmentManager

        // Listeners
        binding.regPersonalToNextPageBtn.setOnClickListener{

            var isEmpty: Boolean = actionValidateField(
                binding.regPersonalNameInput,
                binding.regPersonalNameContainer,
                "Please enter your name")

            if (isEmpty){ return@setOnClickListener }

            isEmpty = actionValidateField(
                binding.regPersonalBirthdateInput,
                binding.regPersonalBirthdateContainer,
                "Please enter your date")

            if (isEmpty){return@setOnClickListener}

            val containError: Boolean = actionValidateAddressField(
                binding.regPersonalAddressInput,
                binding.regPersonalAddressContainer
            )

            if (containError){return@setOnClickListener}

            else{
                val index = binding.radioGroup.checkedRadioButtonId
                lateinit var gender: String
                if(index==0){ gender= "Male"}else{gender="Female"}

                val personalData =
                    UserPersonalInfo(
                        "0",
                        binding.regPersonalNameInput.text.toString(),
                        gender,
                        binding.regPersonalBirthdateInput.text.toString(),
                        binding.regPersonalAddressInput.text.toString(),
                        binding.regPersonalIsOkuCheckbox.isChecked.toString()
                    )

//                val bundle = Bundle()
//                bundle.putString("key","data")
//                val fragment = RegContactInfo()
//                fragment.arguments = bundle

                val action = RegPersonalInfoDirections.actionRegPersonalInfoToRegContactInfo(personalData)
                Navigation.findNavController(it).navigate(action)
//                Navigation.findNavController(it).navigate(R.id.action_reg_personal_info_to_reg_contact_info)
            }
        }

        binding.calendarForDatePick.setOnClickListener(){
            if (fragmentManager != null) {
                datePicker.addOnPositiveButtonClickListener {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.time = Date(it)
                    val selectDate = "${calendar.get(Calendar.DAY_OF_MONTH)}-" +
                            "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)}"
                    binding.regPersonalBirthdateInput.setText(selectDate)
                }
                datePicker.show(fragmentManager, datePicker.toString())
            }
        }

        binding.regPersonalToLogin.setOnClickListener(){
            val goToLogin = getIntentToOtherActivity<LoginActivity>(activity as Activity)
            startActivity(goToLogin)
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

    private fun actionValidateAddressField(addressField : EditText, addressFieldContainer: TextInputLayout): Boolean {

        if (addressField.text.toString() == ""){
            addressFieldContainer.error = "Please enter your address"
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
            addressFieldContainer.error = "Please enter valid address"
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

    private inline fun <reified T : Any>getIntentToOtherActivity(currentActivity: Activity): Intent {
        return Intent(currentActivity, T::class.java)
    }

    private fun getDatePicker(): MaterialDatePicker<Long> {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        return datePicker
    }
}