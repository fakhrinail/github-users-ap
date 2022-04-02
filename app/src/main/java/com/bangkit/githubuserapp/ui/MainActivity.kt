package com.bangkit.githubuserapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.githubuserapp.R
import com.bangkit.githubuserapp.databinding.ActivityThemeBinding
import com.bangkit.githubuserapp.preferences.SettingPreferences
import com.bangkit.githubuserapp.viewmodels.ThemeViewModel
import com.bangkit.githubuserapp.viewmodels.ViewModelThemeFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = SettingPreferences.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelThemeFactory(pref)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val delayPage: Long = 3000
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this@MainActivity, ListProfileActivity::class.java)
                startActivity(intent)
                finish()
            }, delayPage
        )
    }
}