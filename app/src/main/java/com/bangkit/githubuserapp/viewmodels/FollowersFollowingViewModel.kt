package com.bangkit.githubuserapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.githubuserapp.models.FollowerFollowingResponseItem
import com.bangkit.githubuserapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFollowingViewModel : ViewModel() {
    private var _users = MutableLiveData<List<FollowerFollowingResponseItem?>?>()
    val users: LiveData<List<FollowerFollowingResponseItem?>?> = _users
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<FollowerFollowingResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowerFollowingResponseItem>>,
                response: Response<List<FollowerFollowingResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<List<FollowerFollowingResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }

    fun getUserFollower(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<FollowerFollowingResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowerFollowingResponseItem>>,
                response: Response<List<FollowerFollowingResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<List<FollowerFollowingResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}