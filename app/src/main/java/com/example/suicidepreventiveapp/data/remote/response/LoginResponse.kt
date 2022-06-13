package com.example.suicidepreventiveapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class Data(

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("accessToken")
    val accessToken: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("refreshToken")
    val refreshToken: String
) : Parcelable
