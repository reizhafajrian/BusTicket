package com.example.busticketactivity.tiketmenu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ItemTicketAdapter
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.listener.TicketItemListener
import com.example.busticketactivity.pickticket.PickTicketActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_tiket_detail.*


class TiketActivty : AppCompatActivity(),TicketItemListener {
    val TAG = "TicketActivity"

    private lateinit var rvTiket: RecyclerView
    private var datahasil = mutableListOf<ItemDataTiket>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiket_activty)
        intitateUI()
        loadData()

    }

    private fun intitateUI() {
        rvTiket = findViewById(R.id.rv_tiket)
        rvTiket.setHasFixedSize(true)
    }


    private fun loadData() {
        spinner.visibility= View.VISIBLE
        FireBaseRepo().getPost().addOnCompleteListener {
            if (it.isSuccessful) {
                spinner.visibility= View.GONE
                datahasil = it.result!!.toObjects(ItemDataTiket::class.java)
                if(datahasil.isEmpty()){
                    tv_warning.visibility=View.VISIBLE
                    tv_warning.text="Data tiket Tidak Tersedia"
                }
                else{
                rvTiket.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                val listItemAdapter = ItemTicketAdapter(datahasil,this)
                listItemAdapter.notifyDataSetChanged()
                rvTiket.adapter = listItemAdapter
                }
            } else {
                spinner.visibility= View.GONE
                Log.d(TAG,"error")
            }
        }
    }


    override fun onItemClick(Nama: String) {
        when(Nama){
            Nama->{
                val gson=Gson()
                val dataFilter=datahasil.filter {
                    it.nama==Nama
                } as MutableList
                val data=gson.toJson(dataFilter[0])
                Log.d(TAG,"ini nama ${dataFilter}")
                val intent=Intent(this,PickTicketActivity::class.java)
                intent.putExtra("title",Nama)
                intent.putExtra("dataTicket",data)
                startActivity(intent)
            }
//            "DPK-KLATEN"->{
//                Log.d(TAG,"ini nama ${Nama}")
//                val gson=Gson()
//                val data=gson.toJson(datahasil[0])
//                val intent=Intent(this,PickTicketActivity::class.java)
//                intent.putExtra("title",Nama)
//                intent.putExtra("dataTicket",data)
//                startActivity(intent)
//            }
        }

    }
}





