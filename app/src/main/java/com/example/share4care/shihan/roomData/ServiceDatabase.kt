package com.example.share4care.shihan.roomData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OKUserviceDB::class], version = 1, exportSchema = false)
abstract class ServiceDatabase: RoomDatabase() {

    abstract fun serviceDao(): ServiceDao

    companion object{
        @Volatile
        private var INSTANCE:ServiceDatabase? = null

        fun getDatabase(context: Context): ServiceDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ServiceDatabase::class.java,
                    "service_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}