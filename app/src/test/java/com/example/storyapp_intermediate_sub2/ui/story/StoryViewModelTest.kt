package com.example.storyapp_intermediate_sub2.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp_intermediate_sub2.data.adapter.StoryRecyclerAdapter
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.data.repository.AuthRepository
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository
import com.example.storyapp_intermediate_sub2.ui.login.LoginViewModel
import com.example.storyapp_intermediate_sub2.util.Dummy
import com.example.storyapp_intermediate_sub2.util.PagedTestDataSource
import com.example.storyapp_intermediate_sub2.utils.CoroutinesTestRule
import com.example.storyapp_intermediate_sub2.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel

    private var dummyStories = Dummy.generateDummyStoryList()

    @Before
    fun setup(){
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `get all stories successfully fetched`() = runTest {
        val data = PagedTestDataSource.snapshot(dummyStories)

        val stories = MutableLiveData<PagingData<StoryEntity>>()
        stories.value = data

        Mockito.`when`(storyRepository.fetchPagingStory()).thenReturn(stories)

        val actualStories = storyViewModel.getAllStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryRecyclerAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        Mockito.verify(storyRepository).fetchPagingStory()
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
    }

    @Test
    fun `when logout should clear session token`() = runTest{
        storyViewModel.clearSession()
        Mockito.verify(storyRepository).clearSession()
    }


    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}