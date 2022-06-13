package com.example.suicidepreventiveapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.suicidepreventiveapp.data.repository.AuthRepository
import com.example.suicidepreventiveapp.utils.Event

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val toastbarText: LiveData<Event<String>> = authRepository.toastbarText
    val loadingDialog: LiveData<Event<Boolean>> = authRepository.loadingDialog

    fun login(username: String, password: String) = authRepository.login(username, password)
}