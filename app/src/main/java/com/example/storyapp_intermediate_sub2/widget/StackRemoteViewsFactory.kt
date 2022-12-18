package com.example.storyapp_intermediate_sub2.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.data.remote.response.ListStoryItem
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository
import com.example.storyapp_intermediate_sub2.di.Injection

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<ListStoryItem?>()

    override fun onCreate() {
    }

    // Ngambil dari API
    override fun onDataSetChanged() {
        val widgetRepo = Injection.provideStoryRepository(mContext)
        insertWidgetItem(widgetRepo)
    }

    override fun onDestroy() {}

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val currentBanner = mWidgetItems[position]

        val bannerImg = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position]?.photoUrl)
            .submit().get()
        rv.setImageViewBitmap(R.id.imageView, bannerImg)


        val extras = bundleOf(
            StoryWidget.EXTRA_ITEM to (currentBanner?.description ?: "no desc")
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    private fun insertWidgetItem(repo: StoryRepository) {
        val storyResponse = repo.getApiService().fetchStories(null, null, 0).execute()
        val storyList = storyResponse.body()?.listStory
        if (storyResponse.isSuccessful) {
            if (storyList != null) {
                mWidgetItems.addAll(storyList)
            }
        }
    }
}