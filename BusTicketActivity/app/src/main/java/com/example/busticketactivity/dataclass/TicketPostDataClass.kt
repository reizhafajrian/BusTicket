package com.example.busticketactivity.dataclass

import com.example.busticketactivity.firebase.DataClassIsKosong

data class TicketPostDataClass(
    var noplat:String="",
    var id:String="",
    var harga:String="",
    var nama:String="",
    var tanggal: MutableList<String> = mutableListOf(),
    var pergi:String="",
    var terminal:String="",
    var type:String="",
    var driver:String="",
    var position: MutableList<DataClassIsKosong> = mutableListOf()
)

data class TicketPostDataClassNoTanggal(
    var noplat:String="",
    var id:String="",
    var harga:String="",
    var nama:String="",
    var pergi:String="",
    var terminal:String="",
    var type:String="",
    var driver:String=""
)
