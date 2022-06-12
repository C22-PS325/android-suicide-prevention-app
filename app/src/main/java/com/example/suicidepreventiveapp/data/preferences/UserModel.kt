package com.example.suicidepreventiveapp.data.preferences

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var username: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var address: String? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null
) : Parcelable
