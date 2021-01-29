package com.example.busticketactivity.pickticket

import com.example.busticketactivity.firebase.DataClassIsKosong
import java.io.Serializable


data class DataItemPickup(
    val type:String="",
    val id:String="",
    val nama:String="",
    val harga:String="",
    val pergi:String="",
    val terminal:String="",
    val tanggal:String="",
    val position: MutableList<DataClassIsKosong?> = mutableListOf()
):Serializable
