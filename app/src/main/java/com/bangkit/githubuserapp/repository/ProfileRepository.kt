package com.bangkit.githubuserapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.bangkit.githubuserapp.database.Profile
import com.bangkit.githubuserapp.database.ProfileDao
import com.bangkit.githubuserapp.database.ProfileRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ProfileRepository(application: Application) {
    private val mProfilesDao: ProfileDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ProfileRoomDatabase.getDatabase(application)
        mProfilesDao = db.profileDao()
    }

    fun getAllProfiles(): LiveData<List<Profile>> = mProfilesDao.getAllProfiles()

    fun getProfile(username: String): LiveData<Profile?> = mProfilesDao.getProfile(username)

    fun insert(profile: Profile) {
        executorService.execute { mProfilesDao.insert(profile) }
    }

    fun deleteByUsername(username: String) {
        executorService.execute { mProfilesDao.deleteByUsername(username)}
    }
}