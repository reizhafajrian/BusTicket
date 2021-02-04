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
import com.example.busticketactivity.adapter.ManagerAdapter
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.dataclass.InfoTiket
import kotlinx.android.synthetic.main.activity_manager.*
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

@Suppress("DEPRECATION")
class ManagerActivity : AppCompatActivity(), View.OnClickListener {
    private val tag = "ManagerActivity"
    private val dataExcel = mutableListOf<InfoTiket>()
    private var harga: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        getData()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )
        btn_open_logout.setOnClickListener {
            val kenek = getSharedPreferences("manager", Context.MODE_PRIVATE)
            kenek.edit().apply {
                putString("manager", "")
                apply()
            }
            finish()
        }
        btn_download.setOnClickListener(this)
    }

    private fun getData() {
        spinner.visibility = View.VISIBLE
        FireBaseRepo().getPaymentManager().addOnCompleteListener {
            if (it.isSuccessful) {
                spinner.visibility = View.GONE
                val hasil = it.result!!.toObjects(ManagerGetData::class.java)
                if (hasil != null) {
                    val list = mutableListOf<InfoTiket>()
                    for (i in hasil) {
                        for (j in i.data) {
                            list.add(j)
                        }
                    }
                    dataExcel.addAll(list)
                    showdata(list)
                    rv_manager.apply {
                        setHasFixedSize(true)
                        layoutManager =
                            LinearLayoutManager(this@ManagerActivity, RecyclerView.VERTICAL, false)
                        adapter = ManagerAdapter(list)
                    }
                } else {
                    tv_warning.visibility = View.VISIBLE
                    tv_warning.text = "Anda Belum Memiliki Data Pemebelian"
                }
            } else {
                spinner.visibility = View.GONE
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

        val tag = "ManagerActivity"
        for (datas in data) {
            harga += (datas.harga).toInt()
        }
        tv_total.text = harga.toString()
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
        row.createCell(6).setCellValue("Total Penjualan")

        Log.d(tag,"ini harga $harga")
        for (i in 0 until dataExcel.size) {
            val row = sheet.createRow(i + 1)
            row.createCell(0).setCellValue(dataExcel[i].email)
            row.createCell(1).setCellValue(dataExcel[i].harga)
            row.createCell(2).setCellValue(dataExcel[i].nama)
            row.createCell(3).setCellValue(dataExcel[i].nomorKursi)
            row.createCell(4).setCellValue(dataExcel[i].pergi)
            row.createCell(5).setCellValue(dataExcel[i].type)

        }
        row2.createCell(6).setCellValue(harga.toString())
        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        val folder = File("$extStorageDirectory/RekapPenjualanTiket.xls")
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_download -> {
                downloadExcel()
            }
        }
    }
}