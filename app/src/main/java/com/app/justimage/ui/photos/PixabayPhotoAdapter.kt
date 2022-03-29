package com.app.justimage.ui.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.justimage.R
import com.app.justimage.data.GithubUserModel
import com.app.justimage.databinding.RowGithubUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

public class PixabayPhotoAdapter(private val onItemClickListener: OnItemClickListener) : PagingDataAdapter<GithubUserModel, PixabayPhotoAdapter.GithubUserViewHolder>(USER_COMPARATOR) {

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        val binding = RowGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubUserViewHolder(binding)
    }

    inner class GithubUserViewHolder(private val binding: RowGithubUserBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null)
                        onItemClickListener.onItemClick(item)
                }

            }
        }

        fun bind(user: GithubUserModel) {
            binding.apply {
                binding.txtUserLogin.text = user.login
                Glide.with(binding.imgUser)
                        .load(user.avatar_url)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.mipmap.ic_launcher)
                        .into(binding.imgUser)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo: GithubUserModel)
    }


    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<GithubUserModel>() {
            override fun areItemsTheSame(oldItem: GithubUserModel, newItem: GithubUserModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GithubUserModel, newItem: GithubUserModel) = oldItem == newItem
        }
    }
}