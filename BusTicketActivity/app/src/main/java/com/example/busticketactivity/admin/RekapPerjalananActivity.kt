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
import kotlinx.android.synthetic.main.activity_detail_rekap2.*
import kotlinx.android.synthetic.main.activity_pick_ticket.rv_pick_ticket
import kotlinx.android.synthetic.main.activity_pick_ticket.spinner
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_berangkat
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_terminal
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_title_bus
import kotlinx.android.synthetic.main.activity_pick_ticket.tv_type_bus
import kotlinx.android.synthetic.main.activity_reset_tiket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class RekapPerjalananActivity : AppCompatActivity(), ListenerPickTicket,View.OnClickListener {
    private var tanggal = ""
    private var listtemp = listOf<InfoTiket>()
    private var list = mutableListOf<InfoTiket>()
    var tag = "RekapPerjalananActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_tiket)
        getData()
        val currentTime = SimpleDateFormat("dd-M-yyyy", Locale.getDefault()).format(Date())
        btn_download_rekap.setOnClickListener(this)
        Loader(currentTime)
        intitateUI()
        tanggal = currentTime
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

    private fun getData() {
        val newData = DataTiket()
        FireBaseRepo().DetailTiket().addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.documents
                for (i in data) {
                    val hasil = i.toObject(ManagerGetData::class.java)
                    for (i in hasil!!.data) {
                        FireBaseRepo().getUserNama(i.email).addOnCompleteListener {
                            val usernama=it.result!!.toObjects(getName::class.java)
                            i.namaUser=usernama!![0].nama
                        }
                        list.add(i)
                        Log.d(tag, "data tiket $list")
                    }
                }
            }
        }
    }
    private fun downloadExcel() {
        val excel = HSSFWorkbook()
        val sheet = excel.createSheet()
        val row = sheet.createRow(0)
        val row2=sheet.createRow(1)
        row.createCell(0).setCellValue("Email")
        row.createCell(1).setCellValue("Harga")
        row.createCell(2).setCellValue("Tujuan")
        row.createCell(3).setCellValue("Nomor Kursi")
        row.createCell(4).setCellValue("Jam Keberangakatan")
        row.createCell(5).setCellValue("Tanggal Pembelian")
        row.createCell(6).setCellValue("Tipe Bus")
        row.createCell(7).setCellValue("Nama Pembeli")

        for (i in 0 until list.size) {
            val row = sheet.createRow(i + 1)
            row.createCell(0).setCellValue(list[i].email)
            row.createCell(1).setCellValue(list[i].harga)
            row.createCell(2).setCellValue(list[i].nama)
            row.createCell(3).setCellValue(list[i].nomorKursi)
            row.createCell(4).setCellValue(list[i].pergi)
            row.createCell(5).setCellValue(list[i].tanggalBeli)
            row.createCell(6).setCellValue(list[i].type)
            row.createCell(7).setCellValue(list[i].namaUser)
        }
        val extStorageDirectory = getExternalFilesDir(null)?.absolutePath
        val folder = File("$extStorageDirectory/RekapPerjalananAdmin.xls")
        try {
            if (!folder.exists()) {
                folder.createNewFile()
            }
            excel.write(FileOutputStream(folder))
            Toast.makeText(this, "Rekap data berhasil di buat di ${extStorageDirectory}/RekapPerjalananAdmin.xls", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onClicks(nomor: String) {
        val newData = DataTiket()
        when (nomor) {
            nomor -> {
                listtemp = list.filter {
                    it.nama == newData!!.nama && it.id == newData.id && it.harga == newData.harga && it.pergi == newData.pergi && it.terminal == newData.terminal && it.nomorKursi == nomor && it.tanggal==tanggal
                }
                val intent =
                    Intent(this@RekapPerjalananActivity,
                        DetailRekapActivity::class.java)
                val gson = Gson()
                val datajson = gson.toJson(listtemp[0])
                intent.putExtra("detailRekap", datajson)
                startActivity(intent)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_download_rekap->{
                downloadExcel()
            }
        }
    }
}
