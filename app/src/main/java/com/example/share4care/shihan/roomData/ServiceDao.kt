package com.example.share4care.shihan.roomData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addService(service: OKUserviceDB)

    @Query("SELECT * FROM service_table ORDER BY title ASC")
    fun readAllData(): LiveData<List<OKUserviceDB>>


}