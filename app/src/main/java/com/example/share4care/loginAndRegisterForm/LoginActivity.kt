package com.example.share4care.loginAndRegisterForm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.share4care.R
import com.example.share4care.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val binding = getActivityBinding(this)

        val goToRegistrationPage = getIntentToOtherActivity<RegistrationMain>(this)

        binding.toRegButton.setOnClickListener(){
            var isEmpty: Boolean
            isEmpty = actionValidateNameField(binding.inputEmailOrUsername, binding.loginUsernameInputContainer)
            if (isEmpty){ return@setOnClickListener}
            isEmpty = actionValidatePasswordField(binding.inputPassword, binding.loginPasswordContainer)
            if (isEmpty){ return@setOnClickListener}
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
}


