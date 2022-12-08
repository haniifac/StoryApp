package com.example.storyapp_intermediate_sub2.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import com.example.storyapp_intermediate_sub2.databinding.ActivityMainBinding
import com.example.storyapp_intermediate_sub2.ui.register.RegisterActivity
import com.example.storyapp_intermediate_sub2.ui.story.StoryActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.login)
        setContentView(binding.root)

        val userSession = this.let { SessionManager.getInstance(it.dataStore) }
        loginViewModel.putSession(userSession)

        sessionCheck()

        setButtonListner()

        observeLoading()

//        observeLoginResult()

//        observeLoginResponseMessage()

    }

    private fun setButtonListner(){
        binding.btnGotoRegister.setOnClickListener {
            startRegisterActivity()
        }

        binding.btnLogin.setOnClickListener {
            val inputCheck = binding.edLoginEmail.isEmailValid && binding.edLoginPassword.isPassValid
            val userEmail = binding.edLoginEmail.text.toString().trim()
            val userPass = binding.edLoginPassword.text.toString().trim()

            if (inputCheck) {
//                loginViewModel.postLogin(userEmail, userPass)
                postLogin(userEmail, userPass)
            } else {
                showToast(getString(R.string.login_button_validation))
            }
        }
    }

    private fun sessionCheck(){
        this.lifecycleScope.launch {
            Log.e(TAG, "Token = ${loginViewModel.getToken()}")
            val token = loginViewModel.getToken()
            if (token.isNotBlank()) {
                startStoryActivity()
                Log.e(TAG, "getSession: go to FeedFragment")
            }
        }
    }

    private fun postLogin(email: String, pass: String){
        loginViewModel.pLogin(email, pass).observe(this){
            if (it != null) {
                if(!it.error){
                    Log.e(TAG, "login success: goto FeedFragment")
                    loginViewModel.saveSession(it.loginResult.token, it.loginResult.name)
                    startStoryActivity()
                }
            }else{
                Log.e(TAG, "Login Failed")
                showToast(getString(R.string.login_auth_failed))
            }
        }
    }

    private fun observeLoading(){
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun observeLoginResult(){
        loginViewModel.loginResult.observe(this) {
            if (it != null) {
                Log.e(TAG, "${it.name} ${it.token}")
                loginViewModel.saveSession(it.token, it.name)
            }
        }
    }

    private fun observeLoginResponseMessage(){
        loginViewModel.loginResponseMessage.observe(this) {
            if (it != null) {
                Log.e(TAG, it.toString())
                if (it == SUCCESS) {
                    startStoryActivity()
                    Log.e(TAG, "loginResponseMessage: goto FeedFragment")
                } else {
                    Log.e(TAG, "Login Failed")
                    showToast(getString(R.string.login_auth_failed))
                }
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loginProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun startStoryActivity(){
        startActivity(Intent(this, StoryActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

    private fun startRegisterActivity(){
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    companion object {
        private const val TAG = "LoginFragment"
        private const val SUCCESS = "success"
    }
}