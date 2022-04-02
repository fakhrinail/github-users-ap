package com.bangkit.githubuserapp.retrofit

import com.bangkit.githubuserapp.models.SearchResponse
import com.bangkit.githubuserapp.models.DetailResponse
import com.bangkit.githubuserapp.models.FollowerFollowingResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUsersSearch(
        @Query("q") username: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<FollowerFollowingResponseItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<FollowerFollowingResponseItem>>
}