package com.example.suicidepreventiveapp.data.remote.retrofit

import com.example.suicidepreventiveapp.data.remote.response.ImagePredictionResponse
import com.example.suicidepreventiveapp.data.remote.response.LoginResponse
import com.example.suicidepreventiveapp.data.remote.response.RefreshTokenResponse
import com.example.suicidepreventiveapp.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("patients/register")
    fun registerUser(
        @Field("username") name: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("address") address: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("patients/login")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("token/refresh")
    fun getAccessToken(
        @Field("refreshToken") refreshToken: String
    ): Call<RefreshTokenResponse>


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "patients/logout", hasBody = true)
//    @DELETE("patients/logout")
    fun logoutUser(
        @Field("refreshToken") refreshToken: String
    ): Call<Boolean>

    @Multipart
    @POST("images/predict")
    fun imagePrediction(
        @Part File: MultipartBody.Part,
        @Header("Authorization") token: String
    ) : Call<ImagePredictionResponse>
}