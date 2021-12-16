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
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.share4care.R
import com.example.share4care.databinding.FragmentRegAccountInfoBinding
import com.example.share4care.shihan.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class RegAccountInfo : Fragment() {
    private lateinit var binding: FragmentRegAccountInfoBinding
    private val args: RegAccountInfoArgs by navArgs()
    private lateinit var userNameList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        lateinit var accountData: UserAccInfo
        fetchUserNameList()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reg_account_info, container, false)

        binding.regAccToNextPage.setOnClickListener() {
            var isInvalid = actionValidateField(
                binding.regInputEmailUsername,
                binding.regAccUsernameContainer,
                "Please enter a username"
            )
            if (isInvalid) { return@setOnClickListener }

            isInvalid = usernameIsExist(binding.regInputEmailUsername.toString())
            if (isInvalid) { return@setOnClickListener }

            isInvalid = actionValidateField(
                binding.regInputPassword,
                binding.regAccPasswordContainer,
                "Please enter a password"
            )

            if (isInvalid) { return@setOnClickListener }

            isInvalid = actionValidateField(
                binding.regInputPasswordConfirm,
                binding.containerConfirmPasswordInput,
                "Please input a password"
            )

            if (isInvalid) { return@setOnClickListener }

            val password1 = binding.regInputPassword.text.toString()
            val password2 = binding.regInputPasswordConfirm.text.toString()

            if (password1 != password2) {
                binding.containerConfirmPasswordInput.error = "Password not match"
                binding.regInputPassword.error = "Password not match"
            }

            accountData = UserAccInfo(
                binding.regInputEmailUsername.text.toString(),
                binding.regInputPassword.text.toString()
            )
            val userData = userDataAssembling(accountData)
            val companyData = companyDataAssembling()

            uploadCompanyData(companyData)
            uploadUserData(userData)

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
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

    private fun userDataAssembling(accountData: UserAccInfo): UserTableRecord {
        val userData = UserTableRecord(
            args.personalInfo.status.toString(),
            accountData.usernameOrEmail,
            accountData.password,
            args.personalInfo.name.toString(),
            args.personalInfo.gender.toString(),
            args.personalInfo.DOB.toString(),
            args.contactInfo.phoneNumber.toString(),
            args.contactInfo.email.toString(),
            args.personalInfo.address.toString(),
            args.personalInfo.isOKU.toString(),
            args.companyInfo.occupation.toString(),
            args.companyInfo.companyName.toString(),
            "Normal",
            args.contactInfo.imglink.toString()
        )
        return userData
    }

    private fun companyDataAssembling(): CompanyTableRecord {
        val companyData = CompanyTableRecord(
            args.companyInfo.companyName.toString(),
            args.companyInfo.companyEmail.toString(),
            args.companyInfo.tel.toString(),
            args.companyInfo.address.toString(),
            args.companyInfo.website.toString()
        )
        return companyData
    }

    private fun uploadUserData(userRecord : UserTableRecord){
        FirebaseDatabase.getInstance().getReference("User").child(userRecord.userName).setValue(userRecord)
    }

    private fun uploadCompanyData(companyRecord : CompanyTableRecord){
        if (companyRecord.companyName.toString() != ""){
            FirebaseDatabase.getInstance().getReference("Company").child(companyRecord.companyWebsite).setValue(companyRecord)
        }
    }

    private inline fun <reified T : Any> getIntentToOtherActivity(currentActivity: Activity): Intent {
        return Intent(currentActivity, T::class.java)
    }

    private fun actionValidateField(field: TextInputEditText, fieldContainer: TextInputLayout, message: String): Boolean {
        if (field.text.toString() == "") {
            fieldContainer.error = message
            return true
        } else
            fieldContainer.error = null
        return false
    }

    private fun actionValidateUserNameExist(usernameField: TextInputEditText, usernameFieldContainer: TextInputLayout): Boolean {
        val username = usernameField.text.toString()
        if (usernameIsExist(username)) {
            usernameFieldContainer.error = "The username is already exist, try another"
            return true
        } else
            usernameFieldContainer.error = null
        return false
    }

    private fun fetchUserNameList() {
        val tableValueListener = object : ValueEventListener {
            override fun onDataChange(table: DataSnapshot) {
                for (c in table.children){
                    val tempVal = c.child("username").value.toString()
                    userNameList.add(tempVal)
                }
                Log.d("username list", userNameList.toString())
            }
            override fun onCancelled(p0: DatabaseError){

            }
        }
    }

    private fun usernameIsExist(username: String): Boolean {
        return username in userNameList
    }
}



