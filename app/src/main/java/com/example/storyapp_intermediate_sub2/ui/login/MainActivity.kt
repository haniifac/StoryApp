package com.example.storyapp_intermediate_sub2.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.databinding.ActivityMainBinding
import com.example.storyapp_intermediate_sub2.ui.register.RegisterActivity
import com.example.storyapp_intermediate_sub2.ui.story.StoryActivity
import com.example.storyapp_intermediate_sub2.util.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.login)
        setContentView(binding.root)

        sessionCheck()
        setButtonListener()
    }

    private fun setButtonListener(){
        binding.btnGotoRegister.setOnClickListener {
            startRegisterActivity()
        }

        binding.btnLogin.setOnClickListener {
            val inputCheck = binding.edLoginEmail.isEmailValid && binding.edLoginPassword.isPassValid
            val userEmail = binding.edLoginEmail.text.toString().trim()
            val userPass = binding.edLoginPassword.text.toString().trim()

            if (inputCheck) {
                postLogin(userEmail, userPass)
            } else {
                showToast(getString(R.string.login_button_validation))
            }
        }
    }

    private fun sessionCheck(){
        lifecycleScope.launch {
            Log.e(TAG, "Token = ${loginViewModel.getToken()}")
            val token = loginViewModel.getToken()
            if (token.isNotBlank()) {
                startStoryActivity()
                Log.e(TAG, "getSession: go to FeedFragment")
            }
        }
    }

    private fun postLogin(email: String, pass: String){
        showLoading(true)
        loginViewModel.pLogin(email, pass).observe(this){
            if (it != null) {
                if(!it.error){
                    showLoading(false)
                    Log.e(TAG, "login success: goto FeedFragment")
                    loginViewModel.saveSession(it.loginResult.token, it.loginResult.name)
                    startStoryActivity()
                }
            }else{
                showLoading(false)
                Log.e(TAG, "Login Failed")
                showToast(getString(R.string.login_auth_failed))
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
    }
}