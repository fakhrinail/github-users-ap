package com.bangkit.githubuserapp.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.githubuserapp.fragments.UsersListFragment

class ProfilePagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = UsersListFragment()

        val bundle = Bundle()
        bundle.putString(UsersListFragment.USERNAME, username)

        when (position) {
            0 -> bundle.putString(UsersListFragment.RENDER, "followers")
            1 -> bundle.putString(UsersListFragment.RENDER, "following")
        }
        fragment.arguments = bundle

        return fragment
    }
}