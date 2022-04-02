package com.bangkit.githubuserapp.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.githubuserapp.R
import com.bangkit.githubuserapp.adapters.ProfilePagerAdapter
import com.bangkit.githubuserapp.databinding.ActivityProfileBinding
import com.bangkit.githubuserapp.models.DetailResponse
import com.bangkit.githubuserapp.viewmodels.ProfileViewModel
import com.bangkit.githubuserapp.viewmodels.ViewModelDatabaseFactory
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private var favStatus: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        val user = intent.getStringExtra(USER)

        profileViewModel = obtainViewModel(this@ProfileActivity)

        profileViewModel.getUserDetail(user.toString())

        profileViewModel.getFavoriteStatus(user.toString()).observe(this) {
            favStatus = it != null
            if (favStatus == true) {
                binding.fabFav.imageTintList = ColorStateList.valueOf(Color.parseColor("#0A9396"))
                binding.fabFav.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFFFF"))
            } else {
                binding.fabFav.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFFFF"))
                binding.fabFav.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#0A9396"))
            }
        }

        profileViewModel.userDetail.observe(this) {
            setDetailUI(it)
        }


        profileViewModel.isError.observe(this) {
            showError(it)
        }

        profileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.fabFav.setOnClickListener {
            if (favStatus == false) {
                profileViewModel.insertToDb()
                Toast.makeText(this@ProfileActivity, "Added to favorite users!", Toast.LENGTH_SHORT)
                    .show()
                favStatus = true
            } else {
                profileViewModel.deleteFromDb()
                Toast.makeText(
                    this@ProfileActivity,
                    "Removed from favorite users!",
                    Toast.LENGTH_SHORT
                ).show()
                favStatus = false
            }
        }

        val profilePagerAdapter = user?.let { ProfilePagerAdapter(this@ProfileActivity, it) }
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = profilePagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

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

    private fun obtainViewModel(activity: AppCompatActivity): ProfileViewModel {
        val factory = ViewModelDatabaseFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[ProfileViewModel::class.java]
    }

    private fun setDetailUI(userDetail: DetailResponse?) {
        with(binding) {
            tvUsername.text = userDetail?.login
            tvName.text = userDetail?.name
            tvCompany.text = userDetail?.company
            tvLocation.text = userDetail?.location
            tvFollower.text = getString(R.string.follower, userDetail?.followers.toString())
            tvFollowing.text = getString(R.string.following, userDetail?.following.toString())
            tvRepository.text = getString(R.string.repository, userDetail?.publicRepos.toString())
            Glide.with(this@ProfileActivity).load(userDetail?.avatarUrl)
                .into(ivProfileImage)
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

    companion object {
        const val USER = "user"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    }
}