package com.example.busticketactivity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

import kotlinx.android.synthetic.main.activity_reset_tiket.*
import kotlinx.android.synthetic.main.toolbar.*
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
    private var listTujuan= mutableListOf<InfoTiket?>()
    var tag = "RekapPerjalananActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_tiket)
        getData()
        val currentTime = SimpleDateFormat("dd-M-yyyy", Locale.getDefault()).format(Date())
        btn_download_rekap.setOnClickListener(this)
        intitateUI()
        tanggal_berangkat.text=currentTime
        tanggal = currentTime
        ly_spinner.visibility=View.VISIBLE
    }



    private fun intitateUI() {
        val newData = DataTiket()
        tv_jam_berangkat.text=newData!!.pergi
        tv_menu.text=newData.nama

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
                listTujuan.addAll(list.filter { get->
                    get.id==newData!!.id && get.type==newData.type && get.pergi==newData.pergi && get.tanggal==tanggal
                })
                rv_admin_rekap.apply {
                    layoutManager=LinearLayoutManager(this@RekapPerjalananActivity,RecyclerView.VERTICAL,false)
                    adapter=RekapAdapter(listTujuan,this@RekapPerjalananActivity)
                }
                ly_spinner.visibility=View.GONE}
            else{
                ly_spinner.visibility=View.GONE
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

        for (i in 0 until listTujuan.size) {
            val row = sheet.createRow(i + 1)
            row.createCell(0).setCellValue(listTujuan[i]!!.email)
            row.createCell(1).setCellValue(listTujuan[i]!!.harga)
            row.createCell(2).setCellValue(listTujuan[i]!!.nama)
            row.createCell(3).setCellValue(listTujuan[i]!!.nomorKursi)
            row.createCell(4).setCellValue(listTujuan[i]!!.pergi)
            row.createCell(5).setCellValue(listTujuan[i]!!.tanggalBeli)
            row.createCell(6).setCellValue(listTujuan[i]!!.type)
            row.createCell(7).setCellValue(listTujuan[i]!!.namaUser)
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
