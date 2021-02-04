package com.example.busticketactivity.listener

import com.example.busticketactivity.dataclass.InfoTiket

interface ItemDetailListener {
    fun onItemClick(posisi:Int,data: InfoTiket)
}