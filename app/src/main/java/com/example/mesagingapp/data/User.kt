package com.example.mesagingapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val userUID:String,
    val userName: String,
    val userImage: String,
    val userState: String
    ) : Parcelable
