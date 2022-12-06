package com.example.storyapp_intermediate_sub2.ui.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.registerResponse.observe(this) {
            if (!it) {
                showToast(getString(R.string.register_success))
                finish()
            } else {
                showToast(getString(R.string.register_failed))
            }
        }

        binding.btnRegister.setOnClickListener {
            val inputCheck =
                binding.edRegisterEmail.isEmailValid && binding.edRegisterPassword.isPassValid && binding.edRegisterName.isNameValid
            val userName = binding.edRegisterName.text.toString().trim()
            val userEmail = binding.edRegisterEmail.text.toString().trim()
            val userPass = binding.edRegisterPassword.text.toString().trim()

            if (inputCheck) {
                registerViewModel.postRegister(userName, userEmail, userPass)
            } else {
                showToast(getString(R.string.login_button_validation))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.registerProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}