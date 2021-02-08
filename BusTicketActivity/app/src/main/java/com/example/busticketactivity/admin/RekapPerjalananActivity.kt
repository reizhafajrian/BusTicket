package com.example.busticketactivity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.PickTicketAdapter
import com.example.busticketactivity.dataclass.InfoTiket
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.listener.ListenerPickTicket
import com.example.busticketactivity.pickticket.DataItemPickup
import com.example.busticketactivity.dataclass.ItemDataTiket
import com.example.busticketactivity.dataclass.ManagerGetData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pick_ticket.rv_pick_ticket
import kotlinx.android.synthetic.main.activity_pick_ticket.spinner
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_berangkat
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_terminal
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_title_bus
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_type_bus
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class RekapPerjalananActivity : AppCompatActivity(), ListenerPickTicket {
    private var tanggal = ""
    var tag = "RekapPerjalananActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_tiket)
        val currentTime = SimpleDateFormat("dd-M-yyyy", Locale.getDefault()).format(Date())
        Loader(currentTime)
        intitateUI()
        tanggal = currentTime
//        btn_reset.setOnClickListener(this)

    }

    private fun Loader(tanggal: String) {


        val titleDoc = intent.getStringExtra("title")
        val firebaseFirestore = FirebaseFirestore.getInstance()
        spinner.visibility = View.VISIBLE
        firebaseFirestore.collection("Keberangkatan").document(titleDoc.toString())
            .collection("posisi").document(tanggal)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    spinner.visibility = View.GONE
                    val list = it.result!!.toObject(DataItemPickup::class.java)
                    if (list != null) {
                        rv_pick_ticket.layoutManager = GridLayoutManager(this, 5)
                        val listItemAdapter = RekapAdapter(list.posisi, this)
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
            tv_type_bus.text = newData.id
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
        val newData = DataTiket()
        when (nomor) {
            nomor -> {
                FireBaseRepo().DetailTiket(newData!!, nomor).addOnCompleteListener {
                    if (it.isSuccessful) {
                        var contoh = listOf<InfoTiket>()
                        var list= mutableListOf<InfoTiket>()
                        val data = it.result!!.documents
                        for (i in data) {
                            val hasil = i.toObject(ManagerGetData::class.java)
                            for (i in hasil!!.data){
                                list.add(i)
                            }

                            Log.d(tag, "data tiket $list")
                        }
                        contoh = list.filter {
                            it.nama == newData.nama && it.id == newData.id && it.harga == newData.harga && it.pergi == newData.pergi && it.terminal == newData.terminal && it.nomorKursi == nomor
                        }
                        val intent= Intent(this,DetailRekapActivity::class.java)
                        val gson=Gson()
                        val datajson=gson.toJson(contoh)
                        intent.putExtra("detailRekap",datajson)
                        startActivity(intent)
                    }
                }
            }
        }
    }


//    override fun onClick(v: View?) {
//        when(v?.id){
//            R.id.btn_reset ->{
//                reset()
//            }
//        }
//    }
//    private fun reset(){
//
//        val newData = DataTiket()
//        FireBaseRepo().resetTiket(newData!!.id,tanggal)
//        Loader(tanggal)
//        }
}