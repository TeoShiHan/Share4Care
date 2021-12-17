package com.example.share4care.shihan.roomData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>?
    private val repository: UserRepository
    var userTable: List<User>? = null


    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    private fun letDataFlowFromDatabaseToGlobalVariable(owner: LifecycleOwner){
        if (readAllData != null) {
            readAllData.observe(owner, Observer {
                    data -> userTable = data
            })
        }
        else return
    }

    fun fetchUserTableData(owner: LifecycleOwner): List<User>? {
        letDataFlowFromDatabaseToGlobalVariable(owner)
        return userTable
    }


}