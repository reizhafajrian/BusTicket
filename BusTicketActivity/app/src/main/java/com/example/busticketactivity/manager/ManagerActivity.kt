package com.example.busticketactivity.manager

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ItemTicketAdapter
import com.example.busticketactivity.admin.RekapAdapter
import com.example.busticketactivity.admin.RekapPerjalananActivity
import com.example.busticketactivity.admin.getName

import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.dataclass.InfoTiket
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.listener.TicketItemListener
import com.example.busticketactivity.pickticket.DataItemPickup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_manager.*
import kotlinx.android.synthetic.main.activity_manager.spinner
import kotlinx.android.synthetic.main.activity_reset_tiket.*
import kotlinx.android.synthetic.main.item_menu.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.tv_menu
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class ManagerActivity : AppCompatActivity(), View.OnClickListener, TicketItemListener {
    private val tag = "ManagerActivity"
    private var dataExcel = mutableListOf<InfoTiket>()
    private var tanggal = ""
    private var jumlah: Int = 0
    private var listtemp = mutableListOf<DataItemPickup>()
    private var list = mutableListOf<InfoTiket>()
    private var harga: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_manager)
        showlist()
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )
        getData()
        btn_open_logout.setOnClickListener {
            val kenek = getSharedPreferences("manager", Context.MODE_PRIVATE)
            kenek.edit().apply {
                putString("manager", "")
                apply()
            }
            finish()
        }
        tv_menu.text = "Manager"
//        btn_download.setOnClickListener(this)
    }

    private fun showlist() {
        spinner.visibility = View.VISIBLE
        FireBaseRepo().getPost().addOnCompleteListener {
            if (it.isSuccessful) {
                 listtemp = (it.result!!.toObjects(DataItemPickup::class.java))
                spinner.visibility = View.GONE

                if (listtemp != null) {
                    rv_manager.apply {
                        layoutManager =
                            LinearLayoutManager(this@ManagerActivity, RecyclerView.VERTICAL, false)
                        adapter = ItemTicketAdapter(listtemp, this@ManagerActivity)
                    }
                }
            }
        }
    }

    override fun onItemClick(Nama: String) {
        when (Nama) {
            Nama -> {
                val gson = Gson()
                val dataFilter = listtemp.filter {
                    it.id == Nama
                } as MutableList
                val data = gson.toJson(dataFilter[0])
                val intent = Intent(this, DetailRekapManagerActivity::class.java)
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

    private fun showdata(data: MutableList<InfoTiket>) {
        val list = mutableListOf<Int>()
        for (i in data) {
            list.add(i.harga.toInt())
        }
        jumlah = list.sum()
        tv_total.text = jumlah.toString()
    }

    private fun getData() {
        FireBaseRepo().DetailTiket().addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.documents
                for (i in data) {
                    val hasil = i.toObject(ManagerGetData::class.java)
                    for (i in hasil!!.data) {
                        FireBaseRepo().getUserNama(i.email).addOnCompleteListener {
                            val usernama = it.result!!.toObjects(getName::class.java)
                            i.namaUser = usernama!![0].nama
                        }
                        list.add(i)
                        Log.d(tag, "data tiket $list")
                    }
                }

                dataExcel.addAll(list)
                showdata(dataExcel)
            }
        }
    }

    private fun downloadExcel() {
        val excel = HSSFWorkbook()
        val sheet = excel.createSheet()
        val row = sheet.createRow(0)
        val row2 = sheet.createRow(1)
        row.createCell(0).setCellValue("Email")
        row.createCell(1).setCellValue("Harga")
        row.createCell(2).setCellValue("Tujuan")
        row.createCell(3).setCellValue("Nomor Kursi")
        row.createCell(4).setCellValue("Jam Keberangakatan")
        row.createCell(5).setCellValue("Tanggal Pembelian")
        row.createCell(6).setCellValue("Tipe Bus")


        for (i in 0 until dataExcel.size) {
            val row = sheet.createRow(i + 1)
            row.createCell(0).setCellValue(dataExcel[i]!!.email)
            row.createCell(1).setCellValue(dataExcel[i]!!.harga)
            row.createCell(2).setCellValue(dataExcel[i]!!.nama)
            row.createCell(3).setCellValue(dataExcel[i]!!.nomorKursi)
            row.createCell(4).setCellValue(dataExcel[i]!!.pergi)
            row.createCell(5).setCellValue(dataExcel[i]!!.tanggalBeli)
            row.createCell(6).setCellValue(dataExcel[i]!!.type)

        }
        row.createCell(7).setCellValue("Total Harga")
        row2.createCell(7).setCellValue(jumlah.toString())

        val extStorageDirectory = getExternalFilesDir(null)?.absolutePath
        val folder = File("$extStorageDirectory/RekapPenjualanManager.xls")
        try {
            if (!folder.exists()) {
                folder.createNewFile()
            }
            excel.write(FileOutputStream(folder))
            Toast.makeText(this,
                "Rekap data berhasil di buat di ${extStorageDirectory}/RekapPenjualanManager.xls",
                Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.btn_download -> {
//                downloadExcel()
//            }
        }
    }
}