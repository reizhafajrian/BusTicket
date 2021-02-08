package com.example.busticketactivity.driver


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.busticketactivity.R
import com.example.busticketactivity.admin.getName
import com.example.busticketactivity.dataclass.InfoTiket

import com.example.busticketactivity.firebase.FireBaseRepo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.*
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.tv_jam_berangkat
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.tv_nama_text
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.tv_nomor_kursi
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.tv_tanggal_pesan_berangkat
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.tv_tanggal_text
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.tv_terminal_text
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.tv_tipe_bus_text



class DetailDriverTIcketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_driver_t_icket)
        val data=intent.getSerializableExtra("data") as InfoTiket
        getData(data)

    }

    private fun getData(data: InfoTiket) {
        tv_jam_berangkat.text = data.pergi
        tv_nama_text.text = data.nama
        tv_terminal_text.text = data.terminal
        tv_nomor_kursi.text = data.nomorKursi
        tv_tanggal_pesan_berangkat.text=data.tanggalBeli
        tv_tanggal_text.text=data.tanggal
        tv_tipe_bus_text.text = data.type
        FireBaseRepo().getUserNama(data.email).addOnCompleteListener {
            if(it.isSuccessful){
                val hasil=it.result!!.toObjects(getName::class.java)
                tv_title.text=hasil!![0].nama
            }
        }
    }
}