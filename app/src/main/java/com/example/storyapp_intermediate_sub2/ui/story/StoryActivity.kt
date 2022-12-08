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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.data.remote.response.ListStoryItem
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import com.example.storyapp_intermediate_sub2.databinding.ActivityStoryBinding
import com.example.storyapp_intermediate_sub2.data.adapter.FeedRecyclerAdapter
import com.example.storyapp_intermediate_sub2.ui.detailstory.DetailStoryActivity
import com.example.storyapp_intermediate_sub2.ui.login.MainActivity
import com.example.storyapp_intermediate_sub2.ui.map.MapsActivity
import com.example.storyapp_intermediate_sub2.ui.upload.UploadPhotoActivity

class StoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStoryBinding
    private val storyViewModel by viewModels<StoryViewModel>()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
    private lateinit var userSession : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.story_home)
        setContentView(binding.root)

        userSession = this.let { SessionManager.getInstance(it.dataStore) }

        storyViewModel.putSession(userSession)

        subscribeLoading()
//        subscribeStories()


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
                storyViewModel.clearSession(userSession)
                Log.e(TAG, "Session deleted")
                startLoginActivity()
                Log.e(TAG, "Go to loginFragment")
            }
        }
        return true
    }


    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: fetch story")
//        fetchStory()
    }

    private fun fetchStory() {
        storyViewModel.getToken().run {
            storyViewModel.loadFeed(this)
        }
    }

    private fun subscribeLoading() {
        storyViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun subscribeStories() {
        storyViewModel.storiesResponse.observe(this) { storyResponse ->
            if (storyResponse != null) {
                storyResponse.listStory?.let { storyItem -> showRecyclerItem(storyItem) }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.feedProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSelectedStory(
        story: ListStoryItem,
        holder: FeedRecyclerAdapter.FeedViewHolder
    ) {
        startActivity(Intent(this, DetailStoryActivity::class.java).apply {
            putExtra(EXTRA_NAME,story.name.toString())
            putExtra(EXTRA_IMG_URL,story.photoUrl.toString())
            putExtra(EXTRA_DESCRIPTION,story.description.toString())
        })
    }

    private fun showRecyclerItem(stories: List<ListStoryItem?>) {
//        val listItem = ArrayList<ListStoryItem>()
//        for (item in stories) {
//            if (item != null) {
//                listItem.add(item)
//            }
//        }
//        val adapter = FeedRecyclerAdapter(listItem)
//        binding.feedRv.layoutManager = LinearLayoutManager(this)
//        binding.feedRv.adapter = adapter
//
//        adapter.setOnItemClickCallback(object : FeedRecyclerAdapter.OnItemClickCallback {
//            override fun onItemClicked(
//                story: ListStoryItem,
//                holder: FeedRecyclerAdapter.FeedViewHolder
//            ) {
//                showSelectedStory(story, holder)
//            }
//        })

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