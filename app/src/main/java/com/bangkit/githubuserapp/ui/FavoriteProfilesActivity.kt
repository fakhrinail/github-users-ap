package com.bangkit.githubuserapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bangkit.githubuserapp.R
import com.bangkit.githubuserapp.adapters.ProfilesAdapter
import com.bangkit.githubuserapp.databinding.ActivityFavoriteUsersBinding
import com.bangkit.githubuserapp.models.ProfileItem
import com.bangkit.githubuserapp.viewmodels.FavoriteProfilesViewModel
import com.bangkit.githubuserapp.viewmodels.ViewModelDatabaseFactory

class FavoriteProfilesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUsersBinding
    private lateinit var profilesAdapter: ProfilesAdapter
    private lateinit var viewModel: FavoriteProfilesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)

        supportActionBar?.title = getString(R.string.favorites)

        setupRecyclerView()

        viewModel = obtainViewModel(this@FavoriteProfilesActivity)
        viewModel.getAllProfiles().observe(this) { profileList ->
            Log.d("ROOM", profileList.toString())
            if (profileList != null) {
                val convertedProfileList = profileList.map {
                    ProfileItem(
                        it.username, it.avatarUrl
                    )
                }

                profilesAdapter.setProfiles(convertedProfileList)
            }
        }

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.theme_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_theme -> {
                val i = Intent(this, ThemeActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteProfilesViewModel {
        val factory = ViewModelDatabaseFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteProfilesViewModel::class.java]
    }

    private fun setupRecyclerView() {
        profilesAdapter = ProfilesAdapter(this@FavoriteProfilesActivity)
        profilesAdapter.setOnClickedCallback(object : ProfilesAdapter.OnClickedCallback {
            override fun onClicked(profileData: ProfileItem?, appContext: Context) {
                val intent = Intent(appContext, ProfileActivity::class.java)
                intent.putExtra(ProfileActivity.USER, profileData?.login)
                startActivity(intent, null)
            }
        })
        binding.rvProfileList.apply {
            adapter = profilesAdapter
            setHasFixedSize(true)
        }
    }
}