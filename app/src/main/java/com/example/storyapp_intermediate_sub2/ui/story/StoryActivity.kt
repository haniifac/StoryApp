package com.example.storyapp_intermediate_sub2.ui.story

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.data.adapter.LoadingStateAdapter
import com.example.storyapp_intermediate_sub2.data.adapter.StoryRecyclerAdapter
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.databinding.ActivityStoryBinding
import com.example.storyapp_intermediate_sub2.ui.login.MainActivity
import com.example.storyapp_intermediate_sub2.ui.map.MapsActivity
import com.example.storyapp_intermediate_sub2.ui.upload.UploadPhotoActivity
import com.example.storyapp_intermediate_sub2.util.ViewModelFactory


class StoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStoryBinding
    private val storyViewModel: StoryViewModel by viewModels { ViewModelFactory(this)}
    private lateinit var adapterRv : StoryRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.story_home)
        setContentView(binding.root)

        // untuk nampilin recyclerview
        setStoryRecyclerView()

        subscribeGetAllStories()

    }

    override fun onResume() {
        super.onResume()
        // subscriber ditaro di sini biar bisa get new story pas balik dari detailstory
//        subscribeGetAllStories()
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
            R.id.menu_refresh_story -> {
                subscribeGetAllStories()
                smoothScrollToTop()
                Log.e(TAG,"Refresh story adapter")
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

    private fun setStoryRecyclerView(){
        linearLayoutManager = LinearLayoutManager(this)
        binding.feedRv.layoutManager = linearLayoutManager

        adapterRv = StoryRecyclerAdapter()
        binding.feedRv.adapter = adapterRv.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapterRv.retry()
            }
        )
    }

    private fun subscribeGetAllStories(){
        storyViewModel.getAllStories().observe(this){
            getNewestData(it)
        }
    }

    private fun smoothScrollToTop(){
        var smoothScroller: SmoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = 0
        linearLayoutManager.startSmoothScroll(smoothScroller)
    }

    private fun getNewestData(story : PagingData<StoryEntity>){
        val feedRvState = binding.feedRv.layoutManager?.onSaveInstanceState()

        adapterRv.submitData(lifecycle, story)

        binding.feedRv.layoutManager?.onRestoreInstanceState(feedRvState)
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