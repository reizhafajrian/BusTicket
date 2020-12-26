package com.example.busticketactivity.tiketmenu

import java.io.Serializable

data class InfoTiket(
    val email: String = "",
    val harga: String = "",
    val namaBus: String = "",
    val nomorKursi: String = "",
    val pergi:String="",
    val terminal:String="",
    val type:String=""
    ):Serializable