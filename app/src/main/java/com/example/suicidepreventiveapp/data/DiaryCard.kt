package com.example.suicidepreventiveapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiaryCard(
    var title: String,
    var photo: Int
) : Parcelable
