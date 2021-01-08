package com.example.busticketactivity.dataclass

import com.example.busticketactivity.signin.DataTIket
import com.example.busticketactivity.tiketmenu.InfoTiket

data class ManagerGetData(
    val data:MutableList<InfoTiket> = mutableListOf()

)
