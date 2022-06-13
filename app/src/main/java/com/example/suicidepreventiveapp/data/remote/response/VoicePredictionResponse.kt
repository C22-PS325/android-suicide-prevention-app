package com.example.suicidepreventiveapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VoicePredictionResponse(

	@field:SerializedName("response")
	val response: String
) : Parcelable
