package com.example.busticketactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.adapter.ItemTicketAdapter
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.listener.TicketItemListener
import com.example.busticketactivity.tiketmenu.ItemDataTiket
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity(),TicketItemListener {
    private var datahasil = mutableListOf<ItemDataTiket>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        showlist()
        btn_logout.setOnClickListener {
            val prefs=getSharedPreferences("admin",MODE_PRIVATE).edit()
            prefs.putString("admin","")
            prefs.apply()
            finish()
        }
    }

    private fun showlist() {
        spinner.visibility=View.VISIBLE
        FireBaseRepo().getPost().addOnCompleteListener {
            if (it.isSuccessful) {
                datahasil = it.result!!.toObjects(ItemDataTiket::class.java)
                spinner.visibility= View.GONE
               if(datahasil!=null){
                    rv_admin.apply {
                        layoutManager =
                            LinearLayoutManager(this@AdminActivity, RecyclerView.VERTICAL, false)
                        adapter = ItemTicketAdapter(datahasil, this@AdminActivity)
                    }
                }
                else{

                }
            }
        }

    }

    override fun onItemClick(Nama: String) {
        when(Nama){
            Nama->{
                val gson= Gson()
                val dataFilter=datahasil.filter {
                    it.nama==Nama
                } as MutableList
                val data=gson.toJson(dataFilter[0])
                val intent= Intent(this, ResetTiketActivity::class.java)
                intent.putExtra("title",Nama)
                intent.putExtra("dataTicket",data)
                startActivity(intent)
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

}