package com.example.busticketactivity.signin

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserObject(
    val email:String="",
    val lastname:String="",
    val nama:String="",
    val username:String=""
): Parcelable