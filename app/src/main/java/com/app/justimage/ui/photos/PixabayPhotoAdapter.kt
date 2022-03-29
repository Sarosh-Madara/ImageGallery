package com.app.justimage.ui.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.justimage.R
import com.app.justimage.data.PixabayPhoto
import com.app.justimage.databinding.ItemPhotoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class PixabayPhotoAdapter(private val onItemClickListener: OnItemClickListener) : PagingDataAdapter<PixabayPhoto, PixabayPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

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

        fun bind(photo: PixabayPhoto) {
            binding.apply {
                Glide.with(binding.imageView)
                        .load(photo.previewURL)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(binding.imageView)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo: PixabayPhoto)
    }


    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PixabayPhoto>() {
            override fun areItemsTheSame(oldItem: PixabayPhoto, newItem: PixabayPhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PixabayPhoto, newItem: PixabayPhoto) = oldItem == newItem
        }
    }

}