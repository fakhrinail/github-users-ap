package com.bangkit.githubuserapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Profile::class], version = 1)
abstract class ProfileRoomDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: ProfileRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ProfileRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ProfileRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ProfileRoomDatabase::class.java, "profile_database",
                    )
                        .build()
                }
            }
            return INSTANCE as ProfileRoomDatabase
        }
    }
}