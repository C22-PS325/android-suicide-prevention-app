package com.example.suicidepreventiveapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.data.Result
import com.example.suicidepreventiveapp.data.preferences.UserModel
import com.example.suicidepreventiveapp.data.preferences.UserPreferences
import com.example.suicidepreventiveapp.data.remote.response.Data
import com.example.suicidepreventiveapp.databinding.ActivityLoginBinding
import com.example.suicidepreventiveapp.ui.custom.LoadingDialogBar
import com.example.suicidepreventiveapp.ui.viewmodel.LoginViewModel
import com.example.suicidepreventiveapp.ui.viewmodelfactory.LoginModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val factory: LoginModelFactory = LoginModelFactory.getInstance()
    private val viewModel: LoginViewModel by viewModels { factory }

    private lateinit var loadingDialogBar: LoadingDialogBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogBar = LoadingDialogBar(this)

        binding.tvDontHaveAccount.setOnClickListener {
            val registerPage = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(registerPage)
        }

        viewModel.toastbarText.observe(this) {
            it.getContentIfNotHandled()?.let { text ->
                Toast.makeText(
                    this,
                    text,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.loadingDialog.observe(this) {
            it.getContentIfNotHandled()?.let { status ->
                if (status) {
                    loadingDialogBar.showDialog("Mohon Tunggu...")
                } else {
                    loadingDialogBar.hideDialog()
                }
            }
        }

        binding.btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        val username = binding.edtUsername.text.toString()
        val password = binding.edtPassword.text.toString()

        when {
            username.isEmpty() -> {
                binding.edtUsername.error = "Email tidak boleh kosong"
            }
            password.isEmpty() -> {
                binding.edtPassword.error = "Kata Sandi tidak boleh kosong"
            }
            else -> {
                viewModel.login(username, password).observe(this) {result ->
                    if (result != null) {
                        when(result) {
                            is Result.Loading -> {
                                isLoading(false)
                            }

                            is Result.Success -> {
                                isLoading(false)
                                saveUser(result.data.data!!)

                                val homePage = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(homePage)
                                finish()
                            }

                            is Result.Error ->{
                                isLoading(true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveUser(data: Data) {
        val userPreferences = UserPreferences(this)
        val userModel = UserModel()

        userModel.username = data.username
        userModel.email = data.email
        userModel.phone = data.phone
        userModel.address = data.address
        userModel.accessToken = data.accessToken
        userModel.refreshToken = data.refreshToken

        userPreferences.setUser(userModel)
    }

    private fun isLoading(loading: Boolean) {
        binding.apply {
            edtUsername.isEnabled = loading
            edtPassword.isEnabled = loading
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}