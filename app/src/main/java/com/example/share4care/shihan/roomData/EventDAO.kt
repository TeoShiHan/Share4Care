package com.example.share4care.shihan.roomData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun addEvent(event: EventDB)

    @Query("SELECT * FROM event_table ORDER BY title ASC")
        fun readAllData(): LiveData<List<EventDB>>
}