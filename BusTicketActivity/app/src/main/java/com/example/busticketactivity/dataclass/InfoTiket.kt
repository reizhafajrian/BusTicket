package com.example.busticketactivity.dataclass

import java.io.Serializable

data class InfoTiket(
    val id:String="",
    val nama:String="",
    val email: String = "",
    val harga: String = "",
    val nomorKursi: String = "",
    val pergi:String="",
    val tanggal:String="",
    val tanggalBeli:String="",
    val noplat:String="",
    val terminal:String="",
    val type:String=""
    ):Serializable