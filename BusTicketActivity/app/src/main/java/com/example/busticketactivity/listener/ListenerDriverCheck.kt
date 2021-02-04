package com.example.busticketactivity.listener

import com.example.busticketactivity.dataclass.InfoTiket
import com.google.firebase.firestore.DocumentSnapshot

interface ListenerDriverCheck {
    fun itemClick(data:String)
}