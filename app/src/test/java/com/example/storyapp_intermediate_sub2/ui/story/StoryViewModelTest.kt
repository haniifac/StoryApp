package com.example.storyapp_intermediate_sub2.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp_intermediate_sub2.data.repository.AuthRepository
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository
import com.example.storyapp_intermediate_sub2.ui.login.LoginViewModel
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel


}