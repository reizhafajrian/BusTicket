package com.example.busticketactivity.driver


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.InfoTiket
import com.example.busticketactivity.dataclass.ManagerGetData
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.*



class DetailDriverTIcketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_driver_t_icket)
        val data=intent.getSerializableExtra("data") as InfoTiket
        getData(data)

    }

    private fun getData(data: InfoTiket) {
        tv_email.text=data.email
        tv_title_bus.text =data.nama
        tv_type_bus.text =data.type
        tv_nomor.text ="Nomor Kursi :${data.nomorKursi}"
        tv_berangkat.text =data.pergi
        tv_terminal.text =data.terminal

        tv_id.text=data.id
    }
}