package com.bangkit.githubuserapp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelDatabaseFactory private constructor(private val app: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelDatabaseFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelDatabaseFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelDatabaseFactory::class.java) {
                    INSTANCE = ViewModelDatabaseFactory(application)
                }
            }
            return INSTANCE as ViewModelDatabaseFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteProfilesViewModel::class.java)) {
            return FavoriteProfilesViewModel(app) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}