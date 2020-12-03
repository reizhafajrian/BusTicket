package com.example.busticketactivity.pickticket


data class DataItemPickup(
    val nama:String="",
    val harga:String="",
   val position: MutableList<Any?> = mutableListOf<Any?>()
)
