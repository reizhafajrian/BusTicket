package com.example.busticketactivity.firebase

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Response {
        @SerializedName("transaction_status")
        @Expose
        var transaction_status:String=""
}