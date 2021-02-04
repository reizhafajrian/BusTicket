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
    var tanggal:String="",
    val posisi: MutableList<DataClassIsKosong?> = mutableListOf()
):Serializable
