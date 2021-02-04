package com.example.busticketactivity.driver

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ItemTicketAdapter
import com.example.busticketactivity.firebase.FireBaseRepo

import com.example.busticketactivity.listener.TicketItemListener
import com.example.busticketactivity.pickticket.DataItemPickup

import kotlinx.android.synthetic.main.activity_driver.*


class DriverActivity : AppCompatActivity(), TicketItemListener,View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)
//        initiateUi()
        menu.isIconAnimated=false
        btn_open_scan.setOnClickListener(this)
        btn_open_logout.setOnClickListener(this)
        btn_histori.setOnClickListener(this)
        showlist()

    }

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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_open_logout->{
                val kenek = getSharedPreferences("kenek", Context.MODE_PRIVATE)
                kenek.edit().apply {
                    putString("kenek", "")
                    apply()
                }
                finish()
            }
            R.id.btn_open_scan->
                {
                    val intent = Intent(this, OpenCameraActivity::class.java)
                    startActivity(intent)

                }
            R.id.btn_histori->{
                val intent=Intent(this,HistoriActivity::class.java)
                startActivity(intent)
            }

        }
    }
}
