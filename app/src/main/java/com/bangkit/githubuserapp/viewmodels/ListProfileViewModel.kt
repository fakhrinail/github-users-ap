package com.bangkit.githubuserapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.githubuserapp.models.SearchResponse
import com.bangkit.githubuserapp.models.ProfileItem
import com.bangkit.githubuserapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListProfileViewModel : ViewModel() {
    private var _users = MutableLiveData<List<ProfileItem?>?>()
    val users: LiveData<List<ProfileItem?>?> = _users
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersSearch(query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()?.items?.map {
                        ProfileItem(
                            it?.login,
                            it?.avatarUrl
                        )
                    }

                    _isError.value = false
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}