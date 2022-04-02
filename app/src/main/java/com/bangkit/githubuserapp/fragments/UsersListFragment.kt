package com.bangkit.githubuserapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bangkit.githubuserapp.ui.ProfileActivity
import com.bangkit.githubuserapp.adapters.ProfilesAdapter
import com.bangkit.githubuserapp.databinding.FragmentUsersListBinding
import com.bangkit.githubuserapp.models.ProfileItem
import com.bangkit.githubuserapp.viewmodels.FollowersFollowingViewModel

class UsersListFragment : Fragment() {
    private lateinit var profilesAdapter: ProfilesAdapter
    private lateinit var renderParam: String
    private lateinit var usernameParam: String
    private val viewModel: FollowersFollowingViewModel by viewModels()

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            renderParam = it.getString(RENDER).toString()
            usernameParam = it.getString(USERNAME).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profilesAdapter = ProfilesAdapter(requireContext())

        setupRecyclerView()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.isError.observe(viewLifecycleOwner) {
            showError(it)
        }

        if (renderParam == "followers") {
            viewModel.getUserFollower(usernameParam)
        } else {
            viewModel.getUserFollowing(usernameParam)
        }

        viewModel.users.observe(viewLifecycleOwner) { list ->
            val profiles = list?.map {
                ProfileItem(it?.login, it?.avatarUrl)
            }
            profilesAdapter.setProfiles(profiles)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        profilesAdapter.setOnClickedCallback(object : ProfilesAdapter.OnClickedCallback {
            override fun onClicked(profileData: ProfileItem?, appContext: Context) {
                val intent = Intent(appContext, ProfileActivity::class.java)
                intent.putExtra(ProfileActivity.USER, profileData?.login)
                startActivity(intent, null)
            }
        })

        binding.rvUsersList.apply {
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
            Toast.makeText(
                requireContext(),
                "Error has occurred, please try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val RENDER = "render"
        const val USERNAME = "username"
    }
}