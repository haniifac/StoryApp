package com.example.storyapp_intermediate_sub2.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp_intermediate_sub2.data.repository.AuthRepository
import com.example.storyapp_intermediate_sub2.util.Dummy
import com.example.storyapp_intermediate_sub2.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var registerViewModel: RegisterViewModel
    
    private val dummyName = "myname"
    private val dummyEmail = "email@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setup(){
        registerViewModel = RegisterViewModel(authRepository)
    }


    @Test
    fun `when register successful = register response error is false`(){
        val expectedResponse = MutableLiveData<Boolean>()
        expectedResponse.value = false

        Mockito.`when`(authRepository.postRegister(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Assert.assertFalse(actualResponse)

        Mockito.verify(authRepository).postRegister(dummyName, dummyEmail, dummyPassword)
    }

    @Test
    fun `when register failed = register response error is true`(){
        val expectedResponse = MutableLiveData<Boolean>()
        expectedResponse.value = true

        Mockito.`when`(authRepository.postRegister(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Assert.assertTrue(actualResponse)

        Mockito.verify(authRepository).postRegister(dummyName, dummyEmail, dummyPassword)
    }

}