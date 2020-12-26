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
        val tag="ManagerActivity"
        spinner.visibility= View.VISIBLE
        FireBaseRepo().getPaymentManager().addOnCompleteListener {
            if(it.isSuccessful){
                spinner.visibility= View.GONE
                val data = it.result!!.toObjects(ManagerGetData::class.java)
                showdata(data)
                if (data != null) {
                    rv_manager.apply {
                        setHasFixedSize(true)
                        layoutManager =
                            LinearLayoutManager(this@ManagerActivity, RecyclerView.VERTICAL, false)
                        adapter = ManagerAdapter(data)
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

    private fun showdata(data:MutableList<ManagerGetData>) {
        var harga:Int=0
        for(i in data){
            val hargaToInt=(i.harga).toInt()
            harga+=hargaToInt
            tv_total_value.text=harga.toString()
        }

    }
}