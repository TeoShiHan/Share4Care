package com.example.share4care.shihan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class UserViewModel: ViewModel() {
    private lateinit var  _personalInformation: MutableLiveData<UserPersonalInfo>
    val personalInformation: LiveData<UserPersonalInfo> = _personalInformation

    private lateinit var _contactInformation: MutableLiveData<UserContactInfo>
    val contactInformation: LiveData<UserContactInfo> = _contactInformation

    private lateinit var _companyInformation: MutableLiveData<UserCompanyInfo>
    val companyInformation: LiveData<UserCompanyInfo> = _companyInformation

    private lateinit var _accountInformation: MutableLiveData<UserAccInfo>
    val accountInformation: LiveData<UserAccInfo> = _accountInformation


   fun setPersonalInfo(personalInfo: UserPersonalInfo){
       _personalInformation.value = personalInfo
   }

    fun setContactInfo(contactInfo: UserContactInfo){
        _contactInformation.value=contactInfo
    }

    fun setCompanyInfo(companyInfo: UserCompanyInfo){
        _companyInformation.value=companyInfo
    }

    fun setAccInfo(accInfo: UserAccInfo){
        _accountInformation.value=accInfo
    }

    fun pushPersonalInfoToFirebase(){
        FirebaseDatabase.getInstance().getReference("Test").setValue(_personalInformation)
    }

}