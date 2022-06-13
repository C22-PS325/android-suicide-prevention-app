package com.example.suicidepreventiveapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.suicidepreventiveapp.data.Result
import com.example.suicidepreventiveapp.data.remote.response.LoginResponse
import com.example.suicidepreventiveapp.data.remote.response.RegisterResponse
import com.example.suicidepreventiveapp.data.remote.retrofit.ApiService
import com.example.suicidepreventiveapp.utils.Event
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository private constructor(private val apiService: ApiService){

    private val resultRegister = MediatorLiveData<Result<RegisterResponse>>()
    private val resultLoading = MediatorLiveData<Result<LoginResponse>>()
    private val resultLogout = MediatorLiveData<Result<Boolean>>()

    private val _toastbarText = MutableLiveData<Event<String>>()
    val toastbarText: LiveData<Event<String>> = _toastbarText

    private val _loadingDialog = MutableLiveData<Event<Boolean>>()
    val loadingDialog: LiveData<Event<Boolean>> = _loadingDialog

    fun register(name: String, password: String, phone: String, email: String, address: String) : LiveData<Result<RegisterResponse>> {
        resultRegister.value = Result.Loading
        _loadingDialog.value = Event(true)
        val client = apiService.registerUser(name, password, phone, email, address)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _loadingDialog.value = Event(false)
                if (response.isSuccessful) {
                    val result = response.body()!!
                    resultRegister.value = Result.Success(result)
                    _toastbarText.value = Event(result.message!!)
                } else {
                    resultRegister.value = Result.Error("periksa kembali data kamu / periksa jaringan internet")
                    _toastbarText.value = Event("periksa kembali data kamu / periksa jaringan internet")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _loadingDialog.value = Event(false)
                resultRegister.value = Result.Error(t.message.toString())
                _toastbarText.value = Event(t.message.toString())
            }

        })

        return resultRegister
    }

    fun login(username: String, password: String) : LiveData<Result<LoginResponse>> {
        resultLoading.value = Result.Loading
        _loadingDialog.value = Event(true)
        val client = apiService.loginUser(username, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _loadingDialog.value = Event(false)
                if (response.isSuccessful) {
                    val result = response.body()!!
                    resultLoading.value = Result.Success(result)
                    _toastbarText.value = Event(result.message!!)
                } else {
                    resultLoading.value = Result.Error("Email / Password salah")
                    _toastbarText.value = Event("Email / Password salah")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                resultLoading.value = Result.Error(t.message.toString())
                _toastbarText.value = Event(t.message.toString())
                _loadingDialog.value = Event(false)
            }
        })

        return resultLoading
    }

    fun logout(refreshToken: String) : LiveData<Result<Boolean>> {
        resultLogout.value = Result.Loading
        _loadingDialog.value = Event(true)
        val client = apiService.logoutUser(refreshToken)
        client.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                _loadingDialog.value = Event(false)

                if (response.isSuccessful) {
                    resultLogout.value = Result.Success(true)
                    _toastbarText.value = Event("Berhasil Keluar")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                resultLoading.value = Result.Error(t.message.toString())
                _toastbarText.value = Event(t.message.toString())
                _loadingDialog.value = Event(false)
            }

        })

        return resultLogout
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