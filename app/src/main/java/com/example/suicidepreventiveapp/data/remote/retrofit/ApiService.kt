package com.example.suicidepreventiveapp.data.remote.retrofit

import com.example.suicidepreventiveapp.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("patients/register")
    fun registerUser(
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("address") address: String
    ): Call<RegisterResponse>
}