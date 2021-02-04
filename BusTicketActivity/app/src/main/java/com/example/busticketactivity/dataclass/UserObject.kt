package com.example.busticketactivity.dataclass

import android.os.Parcelable
import java.io.Serializable


data class UserObject(
    val email:String="",
    val telepon:String="",
    val nama:String="",
    val role:String="",
    val imageUrl:String=""
): Serializable