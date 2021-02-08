package com.example.busticketactivity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.TicketPostDataClass
import com.example.busticketactivity.firebase.DataClassIsKosong
import com.example.busticketactivity.firebase.FireBaseRepo
import kotlinx.android.synthetic.main.activity_add_bus.*
import kotlinx.android.synthetic.main.activity_add_bus.dd_tipe_bus
import kotlinx.android.synthetic.main.activity_add_bus.dw_berangkat
import kotlinx.android.synthetic.main.activity_add_bus.dw_id_bus
import kotlinx.android.synthetic.main.activity_add_bus.dw_tujuan
import kotlinx.android.synthetic.main.activity_add_bus.ed_driver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddBusActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bus)
        btn_simpan.setOnClickListener(this)
        tujuan()
        berangkat()
        idBus()
        driver()
        tipebus()
    }





    private fun tujuan() {
        val items = mutableListOf("WONOSOBO","BANDUNG","KLATEN","YOGYAKARTA")
        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
        dw_tujuan.setAdapter(adapter)
    }

    private fun berangkat() {
        val items = mutableListOf("DEPOK")
        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
        dw_berangkat.setAdapter(adapter)
    }

    private fun tipebus() {
        val items = mutableListOf("EKONOMI AC","VIP")
        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
        dd_tipe_bus.setAdapter(adapter)
    }

    private fun idBus() {
        val time=System.currentTimeMillis()
        dw_id_bus.setText("$time")
        dw_id_bus.isEnabled=false
        }

    private fun driver() {
        val items = mutableListOf<String>()
        FireBaseRepo().getDriver().addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.toObjects(GetDriverObject::class.java)
                for (i in data) {
                    items.add(i.nama)
                }
                val adapter = ArrayAdapter(this, R.layout.dropdown, items)
                ed_driver.setAdapter(adapter)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_simpan -> {
                var data = TicketPostDataClass()
                data.id = dw_id_bus.text.toString()
                data.nama = "${dw_berangkat.text.toString()}-${dw_tujuan.text.toString()}"
                data.noplat = ed_plat.text.toString()
                data.driver = ed_driver.text.toString()

                if (data.id != ""
                    && data.nama != ""
                    && data.driver != "" && data.noplat != ""
                ) {
                    GlobalScope.launch {
                        try {
                            val data = async {
                                FireBaseRepo().PostBus(data)
                                onBackPressed()
                            }
                            data.await()
                        } catch (e: Exception) {
                            Toast.makeText(this@AddBusActivity,
                                "permintaan gagal",
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Mohon isi semua data", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }
}