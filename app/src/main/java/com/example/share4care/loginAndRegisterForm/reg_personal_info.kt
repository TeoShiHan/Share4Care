package com.example.share4care.loginAndRegisterForm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.share4care.R
import com.example.share4care.databinding.FragmentRegPersonalInfoBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class reg_personal_info : Fragment() {

    private lateinit var binding: FragmentRegPersonalInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Variables
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reg_personal_info, container, false)
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


        }

        binding.calendarForDatePick.setOnClickListener(){
            if (fragmentManager != null) {
                datePicker.show(fragmentManager, "DATE_PICKER")
            }
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

    // private fun validateDateField()

    fun actionValidateAddressField(addressField : EditText): String {

        val address = addressField.text.toString()

        val statesInMalaysia = listOf("Johor",
            "Kedah","Kelantan","Malacca","Negeri Sembilan","Pahang",
            "Perak","Perlis","Sabah","Sarawak","Selangor","Terengganu")

        for(state in statesInMalaysia){
            if (!address.contains(state, ignoreCase = true)){
                return "Please enter a valid address"
            }
        }

        val containPostCodePattern = ".*\\d{5}".toRegex()

        if (!containPostCodePattern.matches(address)){
            return "Please enter postcode"
        }

        return "CORRECT"
    }

    fun getDatePicker(): MaterialDatePicker<Long> {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        return datePicker
    }

    fun checkDigit(number: Int): String? {
        return if (number <= 9) "0$number" else number.toString()
    }
}