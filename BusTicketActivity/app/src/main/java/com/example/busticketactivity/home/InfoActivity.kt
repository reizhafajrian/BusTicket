package com.example.busticketactivity.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.busticketactivity.R
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        initiateUI()
    }

    private fun initiateUI() {
        tv_text.text="Agen Resmi Bus Po. Murni Jaya Depok\n" +
                "Alamat: Jl. Margonda Raya No.24, Depok, Kec. Pancoran Mas, Kota Depok, Jawa Barat 16431\n" +
                "Telepon: 0812-9047-6132\n" +
                "Jam Buka : Setiap hari 08.00-17.00"
    }

}