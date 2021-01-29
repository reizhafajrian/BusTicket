package com.example.busticketactivity

import com.example.busticketactivity.firebase.DataClassIsKosong

data class TicketPostDataClass(
    var id:String="",
    var harga:String="",
    var nama:String="",
    var tanggal:String="",
    var pergi:String="",
    var terminal:String="",
    var type:String="",
    var position: MutableList<DataClassIsKosong> = mutableListOf()
)
