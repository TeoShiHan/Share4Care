package com.example.share4care.shihan.roomData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)

    @Query("SELECT * FROM user_table ORDER BY userName ASC")
    fun readAllData(): LiveData<List<User>>


}