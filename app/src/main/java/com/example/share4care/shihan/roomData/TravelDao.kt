package com.example.share4care.shihan.roomData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TravelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTravel(travel: TravelDB)

    @Query("SELECT * FROM travel_table ORDER BY title ASC")
    fun readAllData(): LiveData<List<TravelDB>>


}