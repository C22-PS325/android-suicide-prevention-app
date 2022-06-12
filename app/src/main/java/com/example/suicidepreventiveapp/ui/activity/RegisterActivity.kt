package com.example.suicidepreventiveapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.data.Result
import com.example.suicidepreventiveapp.databinding.ActivityRegisterBinding
import com.example.suicidepreventiveapp.ui.custom.LoadingDialogBar
import com.example.suicidepreventiveapp.ui.viewmodel.RegisterViewModel
import com.example.suicidepreventiveapp.ui.viewmodelfactory.RegisterModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val factory: RegisterModelFactory = RegisterModelFactory.getInstance()
    private val viewModel: RegisterViewModel by viewModels { factory }

    private lateinit var loadingDialogBar: LoadingDialogBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogBar = LoadingDialogBar(this)

        binding.tvHaveAccount.setOnClickListener {
            finish()
        }

        binding.edtEmail.doAfterTextChanged { 
            val isValidEmail = isValidEmail(it.toString())

            if (!isValidEmail) {
                binding.edtEmail.error = "Email tidak sesuai format"
            }
        }

        binding.btnRegister.setOnClickListener { register() }

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
    }

    private fun register() {
        val name = binding.edtFullname.text.toString()
        val email = binding.edtEmail.text.toString()
        val phone = binding.edtPhoneNumber.text.toString()
        val password = binding.edtPassword.text.toString()

        when {
            name.isEmpty() -> {
                binding.edtFullname.error = "Nama Lengkap tidak boleh kosong"
            }
            email.isEmpty() -> {
                binding.edtEmail.error = "Email tidak boleh kosong"
            }
            !isValidEmail(email) -> {
                binding.edtEmail.error = "Email tidak sesuai format"
            }
            phone.isEmpty() -> {
                binding.edtPhoneNumber.error = "Nomor Telepon tidak boleh kosong"
            }
            password.isEmpty() -> {
                binding.edtPassword.error = "Kata Sandi tidak boleh kosong"
            }
            else -> {
                viewModel.register(name, password, phone, email, "").observe(this) {result ->
                    if (result != null) {
                        when(result) {
                            is Result.Loading -> {
                                isLoading(false)
                            }

                            is Result.Success -> {
                                isLoading(false)
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

    private fun isLoading(loading: Boolean) {
        binding.apply {
            btnRegister.isEnabled = loading
            edtFullname.isEnabled = loading
            edtEmail.isEnabled = loading
            edtPhoneNumber.isEnabled = loading
            edtPassword.isEnabled = loading
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}