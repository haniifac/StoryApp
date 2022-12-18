package com.example.storyapp_intermediate_sub2.ui.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp_intermediate_sub2.data.remote.response.UploadImageResponse
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository
import com.example.storyapp_intermediate_sub2.util.Dummy
import com.example.storyapp_intermediate_sub2.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PhotoUploadViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val folder = TemporaryFolder()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var photoUploadViewModel: PhotoUploadViewModel

    private val dummyUploadResponseFalse = Dummy.generateDummyFileUploadResponseFalse()
    private val dummyUploadResponseTrue = Dummy.generateDummyFileUploadResponseTrue()
    private val dummyDescription = Dummy.generateDummyDescription()

    @Before
    fun setup() {
        photoUploadViewModel = PhotoUploadViewModel(storyRepository)
    }

    @Test
    fun `when File successfully uploaded and uploadImageResponse error should be false`() = runTest {
        val tempFile = folder.newFile("tempFile.jpeg")

        val expectedResponse = MutableLiveData<UploadImageResponse>()
        expectedResponse.value = dummyUploadResponseFalse

        `when`(storyRepository.uploadImage(tempFile, dummyDescription, null)).thenReturn(
            expectedResponse
        )

        val actualResponse =
            photoUploadViewModel.uploadImage(tempFile, dummyDescription, null).getOrAwaitValue()

        Assert.assertEquals(expectedResponse.value!!.message, actualResponse.message)
        Assert.assertFalse(actualResponse.error)

        Mockito.verify(storyRepository).uploadImage(tempFile, dummyDescription, null)
    }

    @Test
    fun `when Upload file failed  and uploadImageResponse error should be true`(): Unit = runTest {
        val tempFile = folder.newFile("tempFile.jpeg")

        val expectedResponse = MutableLiveData<UploadImageResponse>()
        expectedResponse.value = dummyUploadResponseTrue

        `when`(storyRepository.uploadImage(tempFile, dummyDescription, null)).thenReturn(
            expectedResponse
        )

        val actualResponse =
            photoUploadViewModel.uploadImage(tempFile, dummyDescription, null).getOrAwaitValue()

        Assert.assertEquals(expectedResponse.value!!.message, actualResponse.message)
        Assert.assertTrue(actualResponse.error)

        Mockito.verify(storyRepository).uploadImage(tempFile, dummyDescription, null)
    }
}