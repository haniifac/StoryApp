package com.example.storyapp_intermediate_sub2.data.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.databinding.FeedItemBinding
import com.example.storyapp_intermediate_sub2.ui.detailstory.DetailStoryActivity
import com.example.storyapp_intermediate_sub2.ui.story.StoryActivity

/** adapter dari recycler yang ada di story
 * ini pake paging
 */

class StoryRecyclerAdapter: PagingDataAdapter<StoryEntity, StoryRecyclerAdapter.ViewHolder>(DIFF_CALLBACK) {
//    private lateinit var onItemClickCallback: StoryRecyclerAdapter.OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data, holder.itemView.context)
//            holder.itemView.setOnClickListener {
//                onItemClickCallback.onItemClicked(data, holder)
//            }
        }
    }

    inner class ViewHolder(private val binding: FeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryEntity, context: Context) {

            Log.e(TAG, data.toString())

            binding.itemTvTopName.text = data.name
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .placeholder(R.drawable.placeholder_view)
                .error(R.drawable.no_image)
                .into(binding.imgFeed)

            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        binding.root.context as Activity,
                        Pair(binding.itemTvTopName, "name"),
                        Pair(binding.imgFeed, "image")
                    )

                Intent(context, DetailStoryActivity::class.java).also { intent ->
                    intent.putExtra(StoryActivity.EXTRA_NAME, data.name)
                    intent.putExtra(StoryActivity.EXTRA_IMG_URL, data.photoUrl)
                    intent.putExtra(StoryActivity.EXTRA_DESCRIPTION, data.description)

                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    interface OnItemClickCallback {
//        fun onItemClicked(story: StoryEntity, holder: StoryRecyclerAdapter.ViewHolder)
//    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }
        }

        private const val TAG = "StoryRecyclerAdapter"
    }
}