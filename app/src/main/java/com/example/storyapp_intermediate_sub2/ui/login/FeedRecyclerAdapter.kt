package com.example.storyapp_intermediate_sub2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp_intermediate_sub2.data.remote.response.ListStoryItem
import com.example.storyapp_intermediate_sub2.databinding.FeedItemBinding

class FeedRecyclerAdapter(private val storiesList: List<ListStoryItem>) :
    RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class FeedViewHolder(private val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var userName = binding.itemTvTopName
        var image = binding.imgFeed
        var imagePhotoUrl: String? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        with(storiesList[position]) {
            holder.userName.text = this.name
            holder.imagePhotoUrl = this.photoUrl
        }

        Glide.with(holder.itemView.context)
            .load(holder.imagePhotoUrl)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(storiesList[holder.adapterPosition], holder)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(story: ListStoryItem, holder: FeedViewHolder)
    }

    override fun getItemCount(): Int = storiesList.size
}