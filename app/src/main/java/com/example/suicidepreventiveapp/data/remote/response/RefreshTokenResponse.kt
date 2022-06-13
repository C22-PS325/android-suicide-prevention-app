package com.example.suicidepreventiveapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RefreshTokenResponse(

	@field:SerializedName("accessToken")
	val accessToken: String
) : Parcelable
