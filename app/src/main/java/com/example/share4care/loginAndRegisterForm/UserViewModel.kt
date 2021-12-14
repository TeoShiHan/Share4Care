package com.example.share4care.loginAndRegisterForm

import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {
    lateinit var personalInformation : UserPersonalInfo
    lateinit var contactInformation : UserContactInfo
    lateinit var companyInformation : UserCompanyInfo
    lateinit var accountInformation : UserAccInfo

    fun data_assembling(){

    }
}