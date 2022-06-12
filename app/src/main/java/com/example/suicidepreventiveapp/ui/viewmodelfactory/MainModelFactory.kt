package com.example.suicidepreventiveapp.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.suicidepreventiveapp.data.di.Injection
import com.example.suicidepreventiveapp.data.repository.AuthRepository
import com.example.suicidepreventiveapp.ui.viewmodel.LoginViewModel
import com.example.suicidepreventiveapp.ui.viewmodel.MainViewModel
import java.lang.IllegalArgumentException

class MainModelFactory private constructor(private val authRepository: AuthRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(authRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MainModelFactory? = null
        fun getInstance() : MainModelFactory =
            instance ?: synchronized(this) {
                instance ?: MainModelFactory(Injection.provideAuthRepository())
            }.also { instance = it }
    }

}