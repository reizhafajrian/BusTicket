package com.example.busticketactivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.util.Pair
import androidx.core.view.size
import com.example.busticketactivity.firebase.DataClassIsKosong
import com.example.busticketactivity.firebase.FireBaseRepo
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_ticket.*

class AddTicketActivity : AppCompatActivity(), View.OnClickListener {
    val tag = "AddTicketActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ticket)
        list()
        btn_simpan.setOnClickListener(this)
        ly_tanggal.setOnClickListener(this)
        ly_jam.setOnClickListener(this)
    }

    private fun list() {
        val items = mutableListOf<Int>()
        for (i in 1 until 40) {
            items.add(i)
        }
        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
        dropdown.setAdapter(adapter)

    }

    private fun date() {
        val builder: MaterialDatePicker.Builder<Long> =
            MaterialDatePicker.Builder.datePicker()
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
        val picker: MaterialDatePicker<*> = builder.build()
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener {
            ed_tanggal.text = picker.headerText
        }

    }

    private fun time() {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Jam Keberangkatan")
                .build()
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour.toString()
            val minute = picker.minute.toString()
            if (hour.toInt() < 10) {
                if(minute.toInt()<10){
                    ed_jam.text = "0$hour:0$minute"
                }
                else{
                    ed_jam.text = "0$hour:$minute"
                }

            } else {
                if(minute.toInt()<10){
                    ed_jam.text = "$hour:0$minute"
                }
                else{
                    ed_jam.text = "0$hour:$minute"
                }
            }

        }
    }

    private fun getAllData() {
        var data = TicketPostDataClass()
        val isKosong = dropdown.text.toString()

        if(isKosong!=""){
        for (i in 0 until isKosong.toInt()) {
            data.position.add(i, (DataClassIsKosong(isKosong = true, nomor = i.toString())))
        }}
        else{
            Toast.makeText(this, "Mohon isi nomor kursi", Toast.LENGTH_SHORT).show()
        }
        data.id = ed_id_bus.text.toString()
        data.harga = ed_harga.text.toString()
        data.terminal = ed_terminal.text.toString()
        data.nama = ed_tujuan.text.toString()
        data.type = ed_type.text.toString()
        data.tanggal = ed_tanggal.text.toString()
        data.pergi = ed_jam.text.toString()
        if (data.harga != "" && data.id != ""
            && data.terminal != "" && data.nama != "" && data.type != ""
            && data.tanggal != "" && data.position!= mutableListOf<DataClassIsKosong>() && data.pergi!==""
        ) {
            FireBaseRepo().PostTicket(data)
        }
        else{
            Toast.makeText(this, "Mohon isi semua data", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ly_tanggal -> {
                date()
            }
            R.id.ly_jam -> {
                time()
            }
            R.id.btn_simpan -> {
                getAllData()
            }
        }
    }
}