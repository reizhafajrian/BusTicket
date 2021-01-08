package com.example.busticketactivity.signin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ManagerAdapter
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.firebase.FireBaseRepo
import kotlinx.android.synthetic.main.activity_manager.*

class ManagerActivity : AppCompatActivity() {
    private val tag="ManagerActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        getData()
        btn_open_logout.setOnClickListener {
            val kenek = getSharedPreferences("manager", Context.MODE_PRIVATE)
            kenek.edit().apply {
                putString("manager", "")
                apply()
            }
            finish()
        }
    }

    private fun getData() {
        spinner.visibility= View.VISIBLE
        FireBaseRepo().getPaymentManager().addOnCompleteListener {
            if(it.isSuccessful){
                spinner.visibility= View.GONE
                val hasil = it.result!!.toObjects(ManagerGetData::class.java)
                if (hasil != null) {
                    showdata(hasil[0])
                    rv_manager.apply {
                        setHasFixedSize(true)
                        layoutManager =
                            LinearLayoutManager(this@ManagerActivity, RecyclerView.VERTICAL, false)
                        adapter = ManagerAdapter(hasil[0])
                    }
                }else{
                    tv_warning.visibility=View.VISIBLE
                    tv_warning.text="Anda Belum Memiliki Data Pemebelian"
                }
            }else{
                spinner.visibility= View.GONE
            }
        }
    }
    override fun onBackPressed() {

            val intent = Intent(Intent.ACTION_MAIN)
            intent.apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)

    }

    private fun showdata(data:ManagerGetData) {

        val tag="ManagerActivity"
        var harga:Int=0
        for (data in data.data){
            harga+=data.harga.toInt()
        }
        tv_total.text=harga.toString()




    }
}