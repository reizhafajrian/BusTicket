package com.example.busticketactivity.admin

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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity(), TicketItemListener, View.OnClickListener {
    private var datahasil = mutableListOf<DataItemPickup>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        showlist()
        btn_logout.setOnClickListener(this)
        btn_add_driver.setOnClickListener(this)
        btn_add_ticket.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
        menu.isIconAnimated=false
    }

    private fun showlist() {
        spinner.visibility = View.VISIBLE
        FireBaseRepo().getPost().addOnCompleteListener {
            if (it.isSuccessful) {
                datahasil = it.result!!.toObjects(DataItemPickup::class.java)
                spinner.visibility = View.GONE
                if (datahasil != null) {
                    rv_admin.apply {
                        layoutManager =
                            LinearLayoutManager(this@AdminActivity, RecyclerView.VERTICAL, false)
                        adapter = ItemTicketAdapter(datahasil, this@AdminActivity)
                    }
                } else {

                }
            }
        }

    }

    override fun onItemClick(Nama: String) {
        when (Nama) {
            Nama -> {
                val gson = Gson()
                val dataFilter = datahasil.filter {
                    it.id == Nama
                } as MutableList
                val data = gson.toJson(dataFilter[0])
                val intent = Intent(this, ResetTiketActivity::class.java)
                intent.putExtra("title", Nama)
                intent.putExtra("dataTicket", data)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_logout -> {
                val prefs = getSharedPreferences("admin", MODE_PRIVATE).edit()
                prefs.putString("admin", "")
                prefs.apply()
                finish()
            }
            R.id.btn_add_driver ->{
                val intent=Intent(this, AddDriverActivty::class.java)
                startActivity(intent)
            }
            R.id.btn_add_ticket ->{
                val intent=Intent(this, AddTicketActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_cancel ->{
                val intent=Intent(this, AdminCancelActivity::class.java)
                startActivity(intent)
            }
        }
    }

}