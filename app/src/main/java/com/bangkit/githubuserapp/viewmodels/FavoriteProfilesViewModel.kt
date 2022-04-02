package com.bangkit.githubuserapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.githubuserapp.database.Profile
import com.bangkit.githubuserapp.repository.ProfileRepository

class FavoriteProfilesViewModel(application: Application) : ViewModel() {
    private val mProfileRepository: ProfileRepository = ProfileRepository(application)

    fun getAllProfiles(): LiveData<List<Profile>> = mProfileRepository.getAllProfiles()
}