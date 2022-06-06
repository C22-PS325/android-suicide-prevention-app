package com.example.suicidepreventiveapp.data.di

import com.example.suicidepreventiveapp.data.remote.retrofit.ApiConfig
import com.example.suicidepreventiveapp.data.repository.AuthRepository

object Injection {

    fun provideAuthRepository(): AuthRepository {
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService)
    }
}