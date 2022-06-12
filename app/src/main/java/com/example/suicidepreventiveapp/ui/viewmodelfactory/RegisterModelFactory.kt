package com.example.suicidepreventiveapp.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.suicidepreventiveapp.data.di.Injection
import com.example.suicidepreventiveapp.data.repository.AuthRepository
import com.example.suicidepreventiveapp.ui.viewmodel.RegisterViewModel
import java.lang.IllegalArgumentException

class RegisterModelFactory private constructor(private val authRepository: AuthRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: RegisterModelFactory? = null
        fun getInstance() : RegisterModelFactory =
            instance ?: synchronized(this) {
                instance ?: RegisterModelFactory(Injection.provideAuthRepository())
            }.also { instance = it }
    }

}