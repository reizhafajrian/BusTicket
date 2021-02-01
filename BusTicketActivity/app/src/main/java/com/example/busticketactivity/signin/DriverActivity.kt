package com.example.busticketactivity.signin

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ItemTicketAdapter
import com.example.busticketactivity.firebase.FireBaseRepo

import com.example.busticketactivity.listener.TicketItemListener
import com.example.busticketactivity.pickticket.DataItemPickup

import com.example.busticketactivity.tiketmenu.ItemDataTiket

import com.google.zxing.Result

import kotlinx.android.synthetic.main.activity_driver.*
import me.dm7.barcodescanner.zxing.ZXingScannerView


class DriverActivity : AppCompatActivity(), TicketItemListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)
//        initiateUi()
        btn_open_scan.setOnClickListener {
            val intent = Intent(this, OpenCameraActivity::class.java)
            startActivity(intent)

        }
        btn_open_logout.setOnClickListener {
            val kenek = getSharedPreferences("kenek", Context.MODE_PRIVATE)
            kenek.edit().apply {
                putString("kenek", "")
                apply()
            }
            finish()
        }
        showlist()
    }


//    private fun initiateUi() {
//       val fragment=OpenCameraFragment()
//        supportFragmentManager.beginTransaction().replace(
//            R.id.fragment,fragment
//        )
//    }


    private fun showlist() {
        spinner.visibility = View.VISIBLE
        FireBaseRepo().getPost().addOnCompleteListener {
            if (it.isSuccessful) {
                val datahasil = it.result!!.toObjects(DataItemPickup::class.java)
                spinner.visibility = View.GONE
                if (datahasil != null) {
                    rv_item_Tiket.apply {

                        layoutManager =
                            LinearLayoutManager(this@DriverActivity, RecyclerView.VERTICAL, false)
                        adapter = ItemTicketAdapter(datahasil, this@DriverActivity)
                    }
                } else {
                    tv_warning.visibility = View.GONE
                    tv_warning.text = "Data tidak Tersedia"
                }
            }
        }

    }
}
