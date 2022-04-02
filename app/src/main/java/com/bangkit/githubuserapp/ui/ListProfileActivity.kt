package com.bangkit.githubuserapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.bangkit.githubuserapp.R
import com.bangkit.githubuserapp.adapters.ProfilesAdapter
import com.bangkit.githubuserapp.databinding.ActivityListProfileBinding
import com.bangkit.githubuserapp.models.ProfileItem
import com.bangkit.githubuserapp.viewmodels.ListProfileViewModel

class ListProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListProfileBinding
    private lateinit var profilesAdapter: ProfilesAdapter
    private val listProfileViewModel by viewModels<ListProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProfileBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.github_users)

        setupRecyclerView()

        listProfileViewModel.users.observe(this) {
            profilesAdapter.setProfiles(it)
        }

        listProfileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        listProfileViewModel.isError.observe(this) {
            showError(it)
        }

        binding.svUserSearch.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                listProfileViewModel.getUsers(query)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.complete_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_theme -> {
                val i = Intent(this, ThemeActivity::class.java)
                startActivity(i)
                true
            }
            R.id.menu_fav -> {
                val i = Intent(this, FavoriteProfilesActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun setupRecyclerView() {
        profilesAdapter = ProfilesAdapter(this@ListProfileActivity)
        profilesAdapter.setOnClickedCallback(object : ProfilesAdapter.OnClickedCallback {
            override fun onClicked(profileData: ProfileItem?, appContext: Context) {
                val intent = Intent(appContext, ProfileActivity::class.java)
                intent.putExtra(ProfileActivity.USER, profileData?.login)
                startActivity(intent, null)
            }
        })
        binding.rvProfileList.apply {
            adapter = profilesAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(this, "Error has occurred, please try again", Toast.LENGTH_SHORT).show()
        }
    }
}