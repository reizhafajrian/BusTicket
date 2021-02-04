package com.example.busticketactivity.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.DriverAdapterCheck

import com.example.busticketactivity.dataclass.InfoTiket
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.listener.ListenerDriverCheck
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_histori.*
import kotlinx.android.synthetic.main.activity_histori.refresh_layout
import kotlinx.android.synthetic.main.activity_histori.spinner
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class HistoriActivity : AppCompatActivity(),ListenerDriverCheck,SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {
    private var dataExcel = mutableListOf<InfoTiket>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histori)
        download()
        getData()
        btn_download.setOnClickListener(this)
        refresh_layout.setOnRefreshListener(this)
        refresh_layout.post(object : Runnable {
            override fun run() {
                refresh_layout.isRefreshing = true
                getData()
            }

        })
    }

    private fun getData(){
        refresh_layout.isRefreshing = true
        FireBaseRepo().getPaymentManager().addOnCompleteListener {
            if (it.isSuccessful) {
                val hasil = it.result!!.documents
                if (hasil != null) {
                    val list = mutableListOf<String>()
                    for (i in hasil) {
                            list.add(i.id)

                    }

                    rv_driver_check.apply {
                        setHasFixedSize(true)
                        layoutManager =
                            LinearLayoutManager(this@HistoriActivity, RecyclerView.VERTICAL, false)
                        adapter = DriverAdapterCheck(list,this@HistoriActivity)
                    }
                    refresh_layout.isRefreshing = false
                }else{
                    refresh_layout.isRefreshing = false
                }
            }
        }
    }

    private fun download() {
        FireBaseRepo().getPaymentManager().addOnCompleteListener {
            if (it.isSuccessful) {
                val hasil = it.result!!.toObjects(ManagerGetData::class.java)
                if (hasil != null) {
                    val list = mutableListOf<InfoTiket>()
                    for (i in hasil) {
                        for (j in i.data) {
                            list.add(j)
                        }
                    }
                    dataExcel.addAll(list)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_download->{
                downloadExcel()
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
        row.createCell(5).setCellValue("Tipe Bus")

        for (i in 0 until dataExcel.size) {
            val row = sheet.createRow(i + 1)
            row.createCell(0).setCellValue(dataExcel[i].email)
            row.createCell(1).setCellValue(dataExcel[i].harga)
            row.createCell(2).setCellValue(dataExcel[i].nama)
            row.createCell(3).setCellValue(dataExcel[i].nomorKursi)
            row.createCell(4).setCellValue(dataExcel[i].pergi)
            row.createCell(5).setCellValue(dataExcel[i].type)

        }
        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        val folder = File("$extStorageDirectory/data_penumpang_driver.xls")
        try {
            if (!folder.exists()) {
                folder.createNewFile()
            }
            excel.write(FileOutputStream(folder))
            Toast.makeText(this, "Rekap data berhasil di buat di ${extStorageDirectory}/hasil.xls", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    override fun itemClick(data: InfoTiket) {
//        when(data.email){
//            data.email->{
//                val intent=Intent(this,DetailHistoryActivity::class.java)
//                intent.putExtra("dataHistori",data)
//                startActivity(intent)
//            }
//        }
//    }

    override fun onRefresh() {
        getData()
    }



    override fun itemClick(data: String) {
        when(data){
            data->{
                val intent=Intent(this,DetailHistoryActivity::class.java)
                intent.putExtra("dataHistori",data)
                startActivity(intent)
            }
        }
    }
}