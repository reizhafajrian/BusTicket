package com.example.busticketactivity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.InfoTiket
import com.example.busticketactivity.firebase.FireBaseRepo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_rekap2.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailRekapActivity : AppCompatActivity() {
    val gson = Gson()
    var data = ""
    val tag = "DetailRekapActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_rekap2)
        tv_menu.text = "Info Perjalanan"
        data = intent.getStringExtra("detailRekap")
        getData(data)
    }


    private fun getData(data: String) {
        val datas = gson.fromJson(data, InfoTiket::class.java)
        tv_title.text = datas.id
        tv_nama_text.text=datas.nama
        tv_nomor_kursi.text = datas.nomorKursi
        tv_harga_text.text = datas.harga
        tv_jam_berangkat.text = datas.pergi
        tv_tanggal_text.text = datas.tanggalBeli
        tv_tanggal_pesan_berangkat.text = datas.tanggal
        tv_terminal_text.text = datas.terminal
        tv_tipe_bus.text = datas.type
        FireBaseRepo().getUserNama(datas.email).addOnCompleteListener {
            if (it.isSuccessful) {
                val data =
                    it.result?.toObjects(getName::class.java)
                tv_nama_penumpang.text = data!![0].nama
            }
        }
    }
}