package com.example.storyapp_intermediate_sub2.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp_intermediate_sub2.data.remote.response.StoriesResponse
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository
import com.example.storyapp_intermediate_sub2.util.Dummy
import com.example.storyapp_intermediate_sub2.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel

    private val dummyStoryFalse = Dummy.generateDummyStoryResponseFalse()
    private val dummyStoryTrue = Dummy.generateDummyStoryResponseTrue()

    @Before
    fun setup(){
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when loadFeed successful = load feed response error is false`(){
        val expectedResponse = MutableLiveData<StoriesResponse?>()
        expectedResponse.value = dummyStoryFalse

        `when`(storyRepository.fetchStories()).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.loadFeed().getOrAwaitValue()
        if (actualResponse != null) {
            Assert.assertFalse(actualResponse.error)
        }

        Mockito.verify(storyRepository).fetchStories()
    }

    @Test
    fun `when loadFeed failed = load feed response error is true`(){
        val expectedResponse = MutableLiveData<StoriesResponse?>()
        expectedResponse.value = dummyStoryTrue

        `when`(storyRepository.fetchStories()).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.loadFeed().getOrAwaitValue()
        if (actualResponse != null) {
            Assert.assertTrue(actualResponse.error)
        }

        Mockito.verify(storyRepository).fetchStories()
    }
}