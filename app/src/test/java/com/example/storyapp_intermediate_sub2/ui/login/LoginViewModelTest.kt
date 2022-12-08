package com.example.storyapp_intermediate_sub2.ui.login

import com.example.storyapp_intermediate_sub2.util.Dummy
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{

    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = Dummy.generateDummyLoginResponse()
    private val dummyEmail = "name@mail.org"
    private val dummyPassword = "password"
    private val dummyToken = "auth_token"


}