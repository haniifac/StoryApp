package com.example.storyapp_intermediate_sub2.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.detail_story)
        setContentView(binding.root)

        val dataName = intent.getStringExtra(StoryActivity.EXTRA_NAME)
        val dataPhoto = intent.getStringExtra(StoryActivity.EXTRA_IMG_URL)
        val dataDesc = intent.getStringExtra(StoryActivity.EXTRA_DESCRIPTION)

        binding.detailTvName.text = dataName
        binding.detailDesc.text = dataDesc
        if (dataPhoto != null) {
            loadPhoto(dataPhoto)
        }
    }

    private fun loadPhoto(url: String) {
        this.let {
            Glide.with(it)
                .load(url)
                .into(binding.detailImgFeed)
        }

    }
}