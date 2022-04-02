package com.bangkit.githubuserapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.githubuserapp.databinding.ProfileItemBinding
import com.bangkit.githubuserapp.helper.ProfileDiffCallback
import com.bangkit.githubuserapp.models.ProfileItem
import com.bumptech.glide.Glide

class ProfilesAdapter(private val appContext: Context) :
    RecyclerView.Adapter<ProfilesAdapter.ViewHolder>() {
    private lateinit var onClickedCallback: OnClickedCallback
    private var profiles = ArrayList<ProfileItem?>()

    fun setOnClickedCallback(onClickedCallback: OnClickedCallback) {
        this.onClickedCallback = onClickedCallback
    }

    fun setProfiles(profiles: List<ProfileItem?>?) {
        profiles?.let {
            val diffCallback = ProfileDiffCallback(this.profiles.toList(), profiles)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            this.profiles.clear()
            this.profiles.addAll(profiles)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    inner class ViewHolder(val binding: ProfileItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProfileItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val profile = profiles[position]

        with(viewHolder.binding) {
            tvUsername.text = profile?.login
            Glide.with(appContext).load(profile?.avatarUrl).into(ivProfile)
            root.setOnClickListener {
                onClickedCallback.onClicked(profile, appContext)
            }
        }
    }

    override fun getItemCount() = profiles.size

    interface OnClickedCallback {
        fun onClicked(profileData: ProfileItem?, appContext: Context)
    }
}