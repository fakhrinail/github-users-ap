package com.bangkit.githubuserapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.githubuserapp.database.Profile
import com.bangkit.githubuserapp.models.ProfileItem

class ProfileDiffCallback(
    private val oldProfileList: List<ProfileItem?>,
    private val newProfileList: List<ProfileItem?>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldProfileList.size
    }

    override fun getNewListSize(): Int {
        return newProfileList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldProfileList[oldItemPosition]?.login == newProfileList[newItemPosition]?.login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProfile = oldProfileList[oldItemPosition]
        val newProfile = newProfileList[newItemPosition]

        return if (oldProfile !== null && newProfile !== null) {
            oldProfile.login == newProfile.login && oldProfile.avatarUrl == newProfile.avatarUrl
        } else !(oldProfile == null || newProfile == null)
    }
}