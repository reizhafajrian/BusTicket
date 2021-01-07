package com.example.busticketactivity.tiketmenu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.listener.ItemDetailListener
import kotlinx.android.synthetic.main.activity_tiket_detail.*

class TiketDetailActivity : AppCompatActivity(),ItemDetailListener {
    val TAG="TiketDetailActivity"
    private val list= mutableListOf<InfoTiket>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiket_detail)
        getTiketInfo()
    }

    private fun getTiketInfo(){
        spinner.visibility=View.VISIBLE
        val email=getSharedPreferences("email",Context.MODE_PRIVATE)
        val emailData=email.getString("email","").toString()
        Log.d(TAG,"Ini email ${emailData}")
        FireBaseRepo().getPaymentTiket(emailData).addOnCompleteListener {
            if(it.isSuccessful){
                spinner.visibility=View.GONE
                val infoTiket=it.result!!.toObjects(InfoTiket::class.java)
                if(infoTiket.isEmpty()){
                    tv_warning.visibility= View.VISIBLE
                    tv_warning.text="Data pembelian tiket anda tidak ada"
                }
                else{
                list.addAll(infoTiket)
                showList(list)
                Log.d(TAG,"ini data tiket ${infoTiket}")
                }
            }
            else{
                spinner.visibility=View.GONE
            }
        }
    }
    private fun showList(lis:MutableList<InfoTiket>){
        rv_detail_tiket.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this@TiketDetailActivity,RecyclerView.VERTICAL,false)
            adapter=ItemDetailTiketAdapter(lis,this@TiketDetailActivity)
        }

    }

    override fun onItemClick(posisi: Int, data: InfoTiket) {
        when(posisi){
            posisi -> {
                val intent=Intent(this,DetailTiketActivity::class.java)
                intent.putExtra("dataTiketPembayaran",data)
                startActivity(intent)
            }
        }
    }
}