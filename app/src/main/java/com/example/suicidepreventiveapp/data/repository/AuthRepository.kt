package com.example.suicidepreventiveapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.suicidepreventiveapp.data.Result
import com.example.suicidepreventiveapp.data.remote.response.RegisterResponse
import com.example.suicidepreventiveapp.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository private constructor(private val apiService: ApiService){

    private val resultRegister = MediatorLiveData<Result<RegisterResponse>>()

    fun register(name: String, password: String, phone: String, email: String, address: String) : LiveData<Result<RegisterResponse>> {
        resultRegister.value = Result.Loading
        val client = apiService.registerUser(name, password, phone, email, address)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    resultRegister.value = Result.Success(result)
                } else {
                    resultRegister.value = Result.Error("periksa kembali data kamu / periksa jaringan internet")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                resultRegister.value = Result.Error(t.message.toString())
            }

        })

        return resultRegister
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService
        ) : AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }

}