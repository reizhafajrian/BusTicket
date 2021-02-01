package com.example.busticketactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.busticketactivity.firebase.FireBaseRepo
import kotlinx.android.synthetic.main.activity_add_driver_activty.*
import kotlinx.android.synthetic.main.activity_add_driver_activty.btn_simpan
import kotlinx.android.synthetic.main.activity_add_driver_activty.dropdown
import kotlinx.android.synthetic.main.activity_add_ticket.*
import kotlinx.coroutines.*

class AddDriverActivty : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_driver_activty)
        btn_simpan.setOnClickListener(this)
        list()
    }

    private fun list() {
        val items = mutableListOf<String>("User", "Admin", "Manager", "Driver")
        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
        dropdown.setAdapter(adapter)

    }

    private fun getDataDriver() = GlobalScope.launch {
        val data = DriverDataClass()
        data.email = ed_email_driver.text.toString()
        data.nama = ed_nama_driver.text.toString()
        data.pass = ed_password.text.toString()
        data.telepon = ed_phone.text.toString()
        data.role = dropdown.text.toString()
        try {
            val job = async{
                FireBaseRepo().createDriver(data)
            }
            job.await()
        }catch (e:Exception){
            Toast.makeText(this@AddDriverActivty, "Error pastikan internet anda terhubung", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_simpan -> {
                getDataDriver()
                val intent= Intent(this,AdminActivity::class.java)
                startActivity(intent)
            }

        }
    }
}
