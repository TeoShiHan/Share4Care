package com.example.share4care.shihan.roomData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TravelDB::class], version = 1, exportSchema = false)
abstract class TravelDatabase: RoomDatabase() {

    abstract fun travelDao(): TravelDao

    companion object{
        @Volatile
        private var INSTANCE:TravelDatabase? = null

        fun getDatabase(context: Context): TravelDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    "travel_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}