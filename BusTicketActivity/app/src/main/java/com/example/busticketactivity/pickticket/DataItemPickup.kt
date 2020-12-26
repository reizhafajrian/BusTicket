package com.example.busticketactivity.pickticket

import com.example.busticketactivity.firebase.DataClassIsKosong


data class DataItemPickup(
    val nama:String="",
    val harga:String="",
    val pergi:String="",
    val terminal:String="",
   val position: MutableList<DataClassIsKosong?> = mutableListOf()
)
