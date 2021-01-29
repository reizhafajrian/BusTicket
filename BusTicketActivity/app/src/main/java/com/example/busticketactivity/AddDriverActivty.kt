package com.example.busticketactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.busticketactivity.firebase.FireBaseRepo
import kotlinx.android.synthetic.main.activity_add_driver_activty.*

class AddDriverActivty : AppCompatActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_driver_activty)
        btn_simpan.setOnClickListener(this)
    }

    private fun getDataDriver(){
        val data=DriverDataClass()
        data.email=ed_email_driver.text.toString()
        data.nama=ed_nama_driver.text.toString()
        data.pass=ed_password.text.toString()
        data.phone=ed_phone.text.toString()
        FireBaseRepo().createDriver(data)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_simpan->{
                getDataDriver()
            }
        }
    }
}