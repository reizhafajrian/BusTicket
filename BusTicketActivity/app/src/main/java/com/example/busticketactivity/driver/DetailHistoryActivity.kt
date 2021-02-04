package com.example.busticketactivity.driver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.AdminCancelDetailAdapter
import com.example.busticketactivity.adapter.ManagerAdapter
import com.example.busticketactivity.dataclass.InfoTiket
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.firebase.FireBaseRepo
import kotlinx.android.synthetic.main.activity_detail_history.*
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class DetailHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history)
        val data = intent.getStringExtra("dataHistori")
        getData(data)
        tv_title.text=data.toString()
    }

    private fun getData(data: String) {

        FireBaseRepo().BuyDetail(data).addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.toObject(ManagerGetData::class.java)
                rv_ticket_detail.apply {
                    layoutManager = LinearLayoutManager(this@DetailHistoryActivity,
                        RecyclerView.VERTICAL,
                        false)
                    adapter = AdminCancelDetailAdapter(data)
                }

            }
        }
    }


}