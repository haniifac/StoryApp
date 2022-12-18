package com.example.storyapp_intermediate_sub2.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp_intermediate_sub2.data.remote.response.LoginResponse
import com.example.storyapp_intermediate_sub2.data.repository.AuthRepository
import com.example.storyapp_intermediate_sub2.util.Dummy
import com.example.storyapp_intermediate_sub2.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var loginViewModel: LoginViewModel

    private val dummyFalseLoginResponse = Dummy.generateDummyFalseLoginResponse()
    private val dummyTrueLoginResponse = Dummy.generateDummyTrueLoginResponse()
    private val dummyEmail = "email@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setup(){
        loginViewModel = LoginViewModel(authRepository)
    }

    @Test
    fun `when Login successful = response not null and actual response error is false`(){
        val expectedResponse = MutableLiveData<LoginResponse>()
        expectedResponse.value = dummyFalseLoginResponse

        `when`(authRepository.postLogin(dummyEmail,dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.pLogin(dummyEmail, dummyPassword).getOrAwaitValue()

        assertNotNull(actualResponse)
        if (actualResponse != null) {
            Assert.assertFalse(actualResponse.error)
        }

        Mockito.verify(authRepository).postLogin(dummyEmail, dummyPassword)
    }

    @Test
    fun `when Login failed = response not null and actual response error is true`(){
        val expectedResponse = MutableLiveData<LoginResponse>()
        expectedResponse.value = dummyTrueLoginResponse

        `when`(authRepository.postLogin(dummyEmail,dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.pLogin(dummyEmail, dummyPassword).getOrAwaitValue()

        assertNotNull(actualResponse)
        if (actualResponse != null) {
            Assert.assertTrue(actualResponse.error)
        }

        Mockito.verify(authRepository).postLogin(dummyEmail, dummyPassword)
    }
}