package com.example.busticketactivity.signin

import android.os.Parcelable
import java.io.Serializable


data class UserObject(
    val email:String="",
    val lastname:String="",
    val nama:String="",
    val username:String="",
    val imageUrl:String=""
): Serializable