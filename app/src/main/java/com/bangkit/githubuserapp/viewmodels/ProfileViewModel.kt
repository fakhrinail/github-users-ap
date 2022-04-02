package com.bangkit.githubuserapp.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.githubuserapp.database.Profile
import com.bangkit.githubuserapp.models.DetailResponse
import com.bangkit.githubuserapp.repository.ProfileRepository
import com.bangkit.githubuserapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : ViewModel() {
    private val mProfileRepository: ProfileRepository = ProfileRepository(application)

    private var _userDetail = MutableLiveData<DetailResponse?>()
    val userDetail: LiveData<DetailResponse?> = _userDetail
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getUserDetail(username: String) {
        if (_userDetail.value == null) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getUserDetail(username)
            client.enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _userDetail.value = response.body()
                    } else {
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }
            })
        }
    }

    fun getFavoriteStatus(username: String): LiveData<Profile?> {
        return mProfileRepository.getProfile(username)
    }

    fun insertToDb() {
        val username = _userDetail.value?.login
        val avatarUrl = _userDetail.value?.avatarUrl

        val profile = Profile()
        profile.username = username
        profile.avatarUrl = avatarUrl

        mProfileRepository.insert(profile)
    }

    fun deleteFromDb() {
        val username = _userDetail.value?.login

        if (username != null) {
            mProfileRepository.deleteByUsername(username)
        }
    }
}