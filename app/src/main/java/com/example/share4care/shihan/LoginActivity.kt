package com.example.share4care.shihan

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.share4care.HomeActivity
import com.example.share4care.R
import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Travel
import com.example.share4care.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    val database = Firebase.database
    val myUserRef = database.getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val binding = getActivityBinding(this)

        val goToRegistrationPage = getIntentToOtherActivity<RegistrationMain>(this)

        binding.toRegButton.setOnClickListener(){
            var isEmpty: Boolean
            isEmpty = actionValidateNameField(binding.inputEmailOrUsername, binding.loginUsernameInputContainer)
            if (isEmpty){ return@setOnClickListener}

            var inputUserName = binding.inputEmailOrUsername.text.toString()

            isEmpty = actionValidatePasswordField(binding.inputPassword, binding.loginPasswordContainer)
            if (isEmpty){ return@setOnClickListener}

            var inputPwd = binding.inputPassword.text.toString()

            loadUser(object:UserCallback{
                override fun onUserBack(s: UserTableRecord) {
                    if (inputUserName == s.userName){
                        if(inputPwd == s.password){
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }, inputUserName)
        }

        binding.toLoginFromRegAcc.setOnClickListener(){
            startActivity(goToRegistrationPage)
        }
    }

    private fun getActivityBinding(activity: Activity): ActivityLoginBinding {
        return DataBindingUtil.setContentView(activity, R.layout.activity_login)
    }

    private fun actionValidateNameField(nameField :TextInputEditText, nameFieldContainer: TextInputLayout): Boolean{
        if (nameField.text.toString() == ""){
            nameFieldContainer.error = "Please enter your username or email"
            return true
        }
        else
            nameFieldContainer.error=null
        return false
    }

    private fun actionValidatePasswordField(passwordField :TextInputEditText, passwordFieldContainer: TextInputLayout): Boolean{
        if (passwordField.text.toString() == ""){
            passwordFieldContainer.error = "Please enter your password"
            return true
        }
        else
            passwordFieldContainer.error=null
        return false
    }

    private inline fun <reified T : Any>getIntentToOtherActivity(currentActivity:Activity): Intent {
        return Intent(currentActivity, T::class.java)
    }

    private fun loadUser(callback: UserCallback, key:String){
        val ref = myUserRef
        var user = UserTableRecord("", "","","","","","","","","","")
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (c in p0.children) {
                        if (c.child("userName").value.toString() == key){
                            val userName =  c.child("userName").value.toString()
                            val password =  c.child("password").value.toString()
                            val name =  c.child("name").value.toString()
                            val gender = c.child("gender").value.toString()
                            val dob = c.child("dob").value.toString()
                            val phone = c.child("phone").value.toString()
                            val email = c.child("email").value.toString()
                            val address = c.child("address").value.toString()
                            val isOKU = c.child("isOKU").value.toString()
                            val occupation = c.child("occupation").value.toString()
                            val accountType = c.child("accountType").value.toString()

                            user = UserTableRecord(userName, password, name, gender, dob, phone, email, address, isOKU, occupation, accountType)
                        }
                    }
                    callback.onUserBack(user)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        ref.addValueEventListener(refListener)
    }

    interface UserCallback{
        fun onUserBack(s:UserTableRecord)
    }



}


