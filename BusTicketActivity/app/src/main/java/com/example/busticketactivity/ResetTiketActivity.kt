package com.example.busticketactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.busticketactivity.adapter.PickTicketAdapter
import com.example.busticketactivity.firebase.DataClassIsKosong
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.listener.ListenerPickTicket
import com.example.busticketactivity.pickticket.DataItemPickup
import com.example.busticketactivity.tiketmenu.ItemDataTiket
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pick_ticket.*
import kotlinx.android.synthetic.main.activity_pick_ticket.rv_pick_ticket
import kotlinx.android.synthetic.main.activity_pick_ticket.spinner
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_berangkat
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_terminal
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_title_bus
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_type_bus
import kotlinx.android.synthetic.main.activity_reset_tiket.*

class ResetTiketActivity : AppCompatActivity(),ListenerPickTicket,View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_tiket)
        Loader()
        intitateUI()
        btn_reset.setOnClickListener(this)
    }

    private fun Loader() {
        val titleDoc = intent.getStringExtra("title")
        val firebaseFirestore = FirebaseFirestore.getInstance()
        spinner.visibility= View.VISIBLE
        firebaseFirestore.collection("Bus").document(titleDoc.toString())
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    spinner.visibility= View.GONE
                    val list = it.result!!.toObject(DataItemPickup::class.java)
                    if (list != null) {
                        rv_pick_ticket.layoutManager = GridLayoutManager(this, 5)
                        val listItemAdapter = PickTicketAdapter(list.position, this)
                        listItemAdapter.notifyDataSetChanged()
                        rv_pick_ticket.adapter = listItemAdapter
                    }
                }
            }
    }

    private fun intitateUI() {
        val newData = DataTiket()
        if (newData != null) {
            tv_title_bus.text = newData.nama
            tv_type_bus.text = newData.type
            tv_terminal.text = newData.terminal
            tv_berangkat.text = newData.pergi
        }
    }
    private fun DataTiket(): ItemDataTiket? {
        val getDataTicket = intent.getStringExtra("dataTicket")
        val gson = Gson()
        val newData = gson.fromJson(getDataTicket, ItemDataTiket::class.java)
        return newData
    }

    override fun onClick(nomor: String) {

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_reset ->{
                reset()
            }
        }
    }
    private fun reset(){
        val newData = DataTiket()
        FireBaseRepo().resetTiket(newData!!.nama)
        Loader()
        }
}