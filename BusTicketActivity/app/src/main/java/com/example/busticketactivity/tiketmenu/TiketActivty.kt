package com.example.busticketactivity.tiketmenu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ItemTicketAdapter
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.listener.TicketItemListener
import com.example.busticketactivity.pickticket.PickTicketActivity
import com.google.gson.Gson


class TiketActivty : AppCompatActivity(),TicketItemListener {
    val TAG = "TicketActivity"

    private lateinit var rvTiket: RecyclerView
    private var datahasil = mutableListOf<ItemDataTiket>()
    private  var dataObject:ItemDataTiket= ItemDataTiket()
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
        FireBaseRepo().getPost().addOnCompleteListener {
            if (it.isSuccessful) {
                datahasil = it.result!!.toObjects(ItemDataTiket::class.java)
                rvTiket.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                val listItemAdapter = ItemTicketAdapter(datahasil,this)
                listItemAdapter.notifyDataSetChanged()
                rvTiket.adapter = listItemAdapter
            } else {
                Log.d(TAG,"error")
            }
        }
    }


    override fun onItemClick(Nama: String) {
        when(Nama){
            Nama->{
                val gson=Gson()
                val data=gson.toJson(datahasil[0])
                val intent=Intent(this,PickTicketActivity::class.java)
                intent.putExtra("title",Nama)
                intent.putExtra("dataTicket",data)
                startActivity(intent)
            }
        }

    }
}





