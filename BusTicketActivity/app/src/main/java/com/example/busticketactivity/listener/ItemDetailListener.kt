package com.example.busticketactivity.listener

import com.example.busticketactivity.tiketmenu.InfoTiket

interface ItemDetailListener {
    fun onItemClick(posisi:Int,data:InfoTiket)
}