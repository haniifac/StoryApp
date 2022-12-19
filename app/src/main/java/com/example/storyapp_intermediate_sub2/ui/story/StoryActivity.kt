package com.example.storyapp_intermediate_sub2.ui.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.data.remote.response.ListStoryItem
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import com.example.storyapp_intermediate_sub2.databinding.ActivityStoryBinding
import com.example.storyapp_intermediate_sub2.data.adapter.FeedRecyclerAdapter
import com.example.storyapp_intermediate_sub2.data.adapter.LoadingStateAdapter
import com.example.storyapp_intermediate_sub2.data.adapter.StoryRecyclerAdapter
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.ui.detailstory.DetailStoryActivity
import com.example.storyapp_intermediate_sub2.ui.login.MainActivity
import com.example.storyapp_intermediate_sub2.ui.map.MapsActivity
import com.example.storyapp_intermediate_sub2.ui.upload.UploadPhotoActivity
import com.example.storyapp_intermediate_sub2.util.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStoryBinding
    private val storyViewModel: StoryViewModel by viewModels { ViewModelFactory(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.story_home)
        setContentView(binding.root)

        // untuk nampilin recyclerview
        binding.feedRv.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feed_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_story -> {
                startUploadActivity()
                Log.e(TAG, "Go to photoUploadFragment")
            }
            R.id.menu_show_map -> {
                startMapsActivity()
                Log.e(TAG,"Goto MapsActivity")
            }
            R.id.menu_logout -> {
                storyViewModel.clearSession()
                Log.e(TAG, "Session deleted")

                startLoginActivity()
                Log.e(TAG, "Go to loginFragment")
            }
        }
        return true
    }

    private fun getData() {
        val adapter = StoryRecyclerAdapter()
        binding.feedRv.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        adapter.setOnItemClickCallback(object : StoryRecyclerAdapter.OnItemClickCallback{
            override fun onItemClicked(story: StoryEntity, holder: StoryRecyclerAdapter.ViewHolder
            ) {
                showSelectedStory(story)
            }
        })

        binding.feedRv.layoutManager = LinearLayoutManager(this)


        storyViewModel.getAllStories().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun showSelectedStory(story: StoryEntity) {
        startActivity(Intent(this, DetailStoryActivity::class.java).apply {
            putExtra(EXTRA_NAME,story.name.toString())
            putExtra(EXTRA_IMG_URL,story.photoUrl.toString())
            putExtra(EXTRA_DESCRIPTION,story.description.toString())
        }, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun startUploadActivity(){
        startActivity(Intent(this, UploadPhotoActivity::class.java))
    }

    private fun startMapsActivity(){
        startActivity(Intent(this, MapsActivity::class.java))
    }

    private fun startLoginActivity(){
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

    companion object {
        private const val TAG = "FeedFragment"

        const val EXTRA_NAME = "extra_name"
        const val EXTRA_IMG_URL = "extra_img_url"
        const val EXTRA_DESCRIPTION = "extra_description"
    }
}