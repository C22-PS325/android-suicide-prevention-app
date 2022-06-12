package com.example.suicidepreventiveapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.suicidepreventiveapp.data.repository.AuthRepository
import com.example.suicidepreventiveapp.utils.Event

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val toastbarText: LiveData<Event<String>> = authRepository.toastbarText
    val loadingDialog: LiveData<Event<Boolean>> = authRepository.loadingDialog

    fun register(name: String, password: String, phone: String, email: String, address: String) = authRepository.register(name, password, phone, email, address)

}