package com.bangkit.githubuserapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(profile: Profile)

    @Delete
    fun delete(profile: Profile)

    @Query("SELECT * from profile ORDER BY id ASC")
    fun getAllProfiles(): LiveData<List<Profile>>

    @Query("SELECT * from profile WHERE username == :username")
    fun getProfile(username: String): LiveData<Profile?>

    @Query("DELETE from profile WHERE username == :username")
    fun deleteByUsername(username: String)
}