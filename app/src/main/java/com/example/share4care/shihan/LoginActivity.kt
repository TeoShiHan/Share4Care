package com.example.share4care.shihan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.share4care.HomeActivity
import com.example.share4care.R
import com.example.share4care.chooili.AdminHome
import com.example.share4care.databinding.ActivityLoginBinding
import com.example.share4care.shihan.roomData.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.share4care.shihan.roomData.User

class LoginActivity : AppCompatActivity() {

    private lateinit var mUserViewModel: UserViewModel
    val database = Firebase.database
    val myUserRef = database.getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val binding = getActivityBinding(this)
        val goToRegistrationPage = getIntentToOtherActivity<RegistrationMain>(this)

//        if (firstUserRecord != null) {
//            Toast.makeText(this, firstUserRecord.password, Toast.LENGTH_SHORT).show()
//            binding.inputEmailOrUsername.setText(firstUserRecord.userName)
//
//            binding.inputPassword.setText(firstUserRecord.password)
//            Toast.makeText(this, firstUserRecord.userName, Toast.LENGTH_SHORT).show()
//        }

        binding.toRegButton.setOnClickListener() {

            // Testing room database functionality
//            val isConnectedToWIFI = isConnectedToWifi()
//
//            if (!isConnectedToWIFI){
//                val localdata = mUserViewModel.fetchTravelTableData(this)
//                val firstRecord = localdata?.get(0)
//                if (firstRecord != null) {
//                    Toast.makeText(this, firstRecord.userName.toString(), Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this, firstRecord.password.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }

            var isEmpty: Boolean = actionValidateNameField(
                binding.inputEmailOrUsername,
                binding.loginUsernameInputContainer
            )
            if (isEmpty) {
                return@setOnClickListener
            }
            var inputUserName = binding.inputEmailOrUsername.text.toString()
            isEmpty =
                actionValidatePasswordField(binding.inputPassword, binding.loginPasswordContainer)
            if (isEmpty) {
                return@setOnClickListener
            }
            var inputPwd = binding.inputPassword.text.toString()


            if (isConnectedToWifi()) {
                loadUser(object : UserCallback {
                    override fun onUserBack(s: UserTableRecord) {
                        if (inputUserName == s.userName && inputPwd == s.password) {
                            if (s.accountType == "Normal") {
                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                intent.putExtra(USERNAME, inputUserName)
                                intent.putExtra(STATUS, s.status)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@LoginActivity, AdminHome::class.java)
                                startActivity(intent)
                            }
                        } else {
                            val dialog = AlertDialog.Builder(this@LoginActivity)
                                .setTitle("Select A Type")
                                .setMessage("Invalid username or password, please try again")
                                .setPositiveButton("Close", null)
                            dialog.show()
                        }
                    }
                }, inputUserName)
            } else {
                val localDataInUserTable = mUserViewModel.fetchUserTableData(this)
                val firstUserRecord = localDataInUserTable?.get(0)
                if (firstUserRecord != null) {
                    if (inputUserName == firstUserRecord.userName && inputPwd == firstUserRecord.password) {
                        if (firstUserRecord != null) {
                            if (firstUserRecord.accountType == "Normal") {
                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                intent.putExtra(USERNAME, inputUserName)
                                intent.putExtra(STATUS, firstUserRecord.status)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@LoginActivity, AdminHome::class.java)
                                startActivity(intent)
                            }
                        }
                    } else {
                        val dialog = AlertDialog.Builder(this@LoginActivity)
                            .setTitle("Select A Type")
                            .setMessage("Invalid username or password, please try again")
                            .setPositiveButton("Close", null)
                        dialog.show()
                    }
                }

            }
        }

        binding.toLoginFromRegAcc.setOnClickListener() {
            startActivity(goToRegistrationPage)
        }

        mUserViewModel.readAllData.observe(this, Observer {
                dataReturnedByFuncton ->

                val userTableRecord = dataReturnedByFuncton
                val firstRecord = dataReturnedByFuncton[0]
                awaitFunFillFieldWithData(binding, firstRecord.password, firstRecord.userName)

        })

}
private fun awaitFunFillFieldWithData(binding: ActivityLoginBinding, password: String, username: String){
    binding.inputPassword.setText(password)
    binding.inputEmailOrUsername.setText(username)
}

private fun getActivityBinding(activity: Activity): ActivityLoginBinding {
    return DataBindingUtil.setContentView(activity, R.layout.activity_login)
}

private fun actionValidateNameField(
    nameField: TextInputEditText,
    nameFieldContainer: TextInputLayout
): Boolean {
    if (nameField.text.toString() == "") {
        nameFieldContainer.error = "Please enter your username or email"
        return true
    } else
        nameFieldContainer.error = null
    return false
}

private fun actionValidatePasswordField(
    passwordField: TextInputEditText,
    passwordFieldContainer: TextInputLayout
): Boolean {
    if (passwordField.text.toString() == "") {
        passwordFieldContainer.error = "Please enter your password"
        return true
    } else
        passwordFieldContainer.error = null
    return false
}

private inline fun <reified T : Any> getIntentToOtherActivity(currentActivity: Activity): Intent {
    return Intent(currentActivity, T::class.java)
}

private fun loadUser(callback: UserCallback, key: String) {
    val ref = myUserRef
    var user = UserTableRecord("", "", "", "", "", "", "", "", "", "", "", "", "", "")
    val refListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            if (p0.exists()) {
                for (c in p0.children) {
                    if (c.child("userName").value.toString() == key) {
                        val status = c.child("status").value.toString()
                        val userName = c.child("userName").value.toString()
                        val password = c.child("password").value.toString()
                        val name = c.child("name").value.toString()
                        val gender = c.child("gender").value.toString()
                        val dob = c.child("dob").value.toString()
                        val phone = c.child("phone").value.toString()
                        val email = c.child("email").value.toString()
                        val address = c.child("address").value.toString()
                        val isOKU = c.child("isOKU").value.toString()
                        val occupation = c.child("occupation").value.toString()
                        val accountType = c.child("accountType").value.toString()
                        val companyName = c.child("companyName").value.toString()
                        val imageLink = c.child("imageLink").value.toString()

                        user = UserTableRecord(
                            status,
                            userName,
                            password,
                            name,
                            gender,
                            dob,
                            phone,
                            email,
                            address,
                            isOKU,
                            occupation,
                            companyName,
                            accountType,
                            imageLink
                        )
                        insertDataToDatabase(user)
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


interface UserCallback {
    fun onUserBack(s: UserTableRecord)
}


    companion object {
    const val USERNAME = "com.example.share4care.USERNAME"
    const val STATUS = "com.example.share4care.STATUS"
}

private fun insertDataToDatabase(user: UserTableRecord) {
    val userData = User(
        user.status,
        user.userName,
        user.password,
        user.name,
        user.gender,
        user.dob,
        user.phone,
        user.email,
        user.address,
        user.isOKU,
        user.occupation,
        user.companyName,
        user.accountType,
        user.imageLink
    )
    mUserViewModel.addUser(userData)
}

fun isConnectedToWifi(): Boolean {
    val context = this.applicationContext
    val connectivity: ConnectivityManager? = null
    var info: NetworkInfo? = null

    val networkManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE)
    val isConnectedWifi = connectivity?.activeNetworkInfo

    if (isConnectedWifi != null && isConnectedWifi.state == NetworkInfo.State.CONNECTED) {
        return true
    } else {
        Toast.makeText(context, "wifi not turned on, local data is applied", Toast.LENGTH_SHORT)
            .show()
        return false
    }
}

}


