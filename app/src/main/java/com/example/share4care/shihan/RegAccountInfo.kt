package com.example.share4care.shihan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.share4care.R
import com.example.share4care.databinding.FragmentRegAccountInfoBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class RegAccountInfo : Fragment() {
    private lateinit var binding: FragmentRegAccountInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val userViewModel: UserViewModel by activityViewModels()
        lateinit var accountData: UserAccInfo
        lateinit var personalData: UserPersonalInfo

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reg_account_info, container, false)

        binding.regAccToNextPage.setOnClickListener() {
            var isInvalid = actionValidateField(
                binding.regInputEmailUsername,
                binding.regAccUsernameContainer,
                "Please enter a username"
            )
            if (isInvalid) {
                return@setOnClickListener
            }
            isInvalid = actionValidateField(
                binding.regInputPassword,
                binding.regAccPasswordContainer,
                "Please enter a password"
            )
            if (isInvalid) {
                return@setOnClickListener
            }
            isInvalid = actionValidateField(
                binding.regInputPasswordConfirm,
                binding.containerConfirmPasswordInput,
                "Please input a password"
            )
            if (isInvalid) {
                return@setOnClickListener
            }
            val password1 = binding.regInputPassword.text.toString()
            val password2 = binding.regInputPasswordConfirm.text.toString()
            if (password1 != password2) {
                binding.containerConfirmPasswordInput.error = "Password not match"
                binding.regInputPassword.error = "Password not match"
            } else {

                accountData = UserAccInfo(
                    binding.regInputEmailUsername.text.toString(),
                    binding.regInputPassword.text.toString()
                )

                userViewModel.personalInformation

                val goToLogin = getIntentToOtherActivity<LoginActivity>(activity as Activity)
                startActivity(goToLogin)
            }
        }

        binding.regAccToPrevPage.setOnClickListener() {
            Navigation.findNavController(it).navigate(R.id.action_regAccountInfo_to_regCompanyInfo)
        }

        binding.toLoginFromRegAcc.setOnClickListener() {
            val toLogin = getIntentToOtherActivity<LoginActivity>(activity as Activity)
            startActivity(toLogin)
        }

        return binding.root
    }

//    private fun dataAssembling(userViewModel: UserViewModel) {
//
//
//
//        userViewModel.userTableRecord = userTableRecord
//
//
//        if (companyInformation.companyName != "") {
//
//            val companyTableRecord = CompanyTableRecord(
//                companyInformation.companyName,
//                companyInformation.tel,
//                companyInformation.companyEmail,
//                companyInformation.address
//            )
//
//            userViewModel.companyTableRecord = companyTableRecord
//
//            FirebaseDatabase.getInstance()
//                .getReference("Company")
//                .child(companyInformation.website)
//                .setValue(companyTableRecord)
//
//            FirebaseDatabase.getInstance()
//                .getReference("Company")
//                .child(companyInformation.website)
//                .child(accountInformation.usernameOrEmail)
//                .setValue(companyInformation.occupation)
//        }
//    }


    private inline fun <reified T : Any> getIntentToOtherActivity(currentActivity: Activity): Intent {
        return Intent(currentActivity, T::class.java)
    }

    private fun actionValidateField(
        field: TextInputEditText,
        fieldContainer: TextInputLayout,
        message: String
    ): Boolean {
        if (field.text.toString() == "") {
            fieldContainer.error = message
            return true
        } else
            fieldContainer.error = null
        return false
    }
}



