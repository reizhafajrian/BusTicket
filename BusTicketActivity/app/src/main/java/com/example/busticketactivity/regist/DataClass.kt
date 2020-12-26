package com.example.busticketactivity.regist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    var Username:String?="",
    var password:String?="",
    var email:String?=""
): Parcelable