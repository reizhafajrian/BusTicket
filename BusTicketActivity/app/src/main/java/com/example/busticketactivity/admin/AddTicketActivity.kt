package com.example.busticketactivity.admin

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.core.view.get
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.TicketPostDataClass
import com.example.busticketactivity.firebase.DataClassIsKosong
import com.example.busticketactivity.firebase.FireBaseRepo
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_add_bus.*
import kotlinx.android.synthetic.main.activity_add_ticket.*
import kotlinx.android.synthetic.main.activity_add_ticket.btn_simpan
import kotlinx.android.synthetic.main.activity_add_ticket.dd_tipe_bus
import kotlinx.android.synthetic.main.activity_add_ticket.dw_berangkat
import kotlinx.android.synthetic.main.activity_add_ticket.dw_id_bus
import kotlinx.android.synthetic.main.activity_add_ticket.ed_driver
import kotlinx.android.synthetic.main.activity_add_ticket.ed_plat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AddTicketActivity : AppCompatActivity(), View.OnClickListener {
    val tag = "AddTicketActivity"
    private var list = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ticket)
        idBus()
        btn_simpan.setOnClickListener(this)
        ly_tanggal.setOnClickListener(this)
        ly_jam.setOnClickListener(this)
        terminal()
        tipebus()
        dw_id_bus.setOnItemClickListener { adapterView, view, i, l ->
            writeDate()
        }
    }






    private fun tipebus() {
        val items = mutableListOf("EKONOMI AC", "VIP")
        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
        dd_tipe_bus.setAdapter(adapter)
    }

    private fun idBus() {
        FireBaseRepo().getIdBus().addOnCompleteListener {
            val listIdBus = mutableListOf<String>()
            if (it.isSuccessful) {
                val data = it.result!!.documents
                for (i in data) {
                    listIdBus.add(i.id)
                }
                val adapter = ArrayAdapter(this, R.layout.dropdown, listIdBus)
                dw_id_bus.setAdapter(adapter)
            }
        }
    }
    private fun terminal() {
        val terminal = mutableListOf<String>("JatiJajar")

        val adapter = ArrayAdapter(this, R.layout.dropdown, terminal)
        dw_terminal.setAdapter(adapter)
    }


    private fun date() {
        list.removeAll(list)
        val builder: MaterialDatePicker.Builder<Pair<Long, Long>> =
            MaterialDatePicker.Builder.dateRangePicker()
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
        builder.setCalendarConstraints(limitrange().build())
        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())

        picker.addOnPositiveButtonClickListener {
            ed_tanggal.text = picker.headerText
//            val remove=picker.headerText.removeRange(5,13)
            val startDate = it.first
            val endDate = it.second
            val msDiff = endDate?.minus(startDate!!)
            val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff!!)

            val sdf = SimpleDateFormat("dd-M-yyyy")
            val calender = Date(startDate!!)
            val date = sdf.format(Date(startDate!!))
            val c = Calendar.getInstance()
            c.time = calender
            c.time = sdf.parse(date)
            for (i in 0..daysDiff.toInt()) {
                val data = sdf.format(c.time)
                list.add(i, data)
                Log.d(tag, "datanya ${data}")
                c.add(Calendar.DATE, 1)
            }
            for (i in list) {
                Log.d(tag, "ini tanggan ${i}")
            }
            Log.d(tag, "ini list $list")
        }
    }

    private fun limitrange(): CalendarConstraints.Builder {
        val calender = CalendarConstraints.Builder()
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        val days = SimpleDateFormat("d")
        val months = SimpleDateFormat("M")
        val years = SimpleDateFormat("yyyy")
        val day = days.format(Date())
        val month = months.format(Date())
        val newYear = years.format(Date())


        val year = newYear.toInt()
        val startMonth = month.toInt()
        val startDate = day.toInt() + 1

        val endMonth = month.toInt() + 1
        val endDate = day.toInt() + 5
        calendarStart[year, startMonth - 1] = startDate - 1
        calendarEnd[year, endMonth - 1] = endDate

        val minDate = calendarStart.timeInMillis
        val maxDate = calendarEnd.timeInMillis


        calender.setStart(minDate)
        calender.setEnd(maxDate)
        calender.setValidator(RangeValidator(minDate, maxDate))

        return calender
    }


    internal class RangeValidator : DateValidator {
        var minDate: Long
        var maxDate: Long

        constructor(minDate: Long, maxDate: Long) {
            this.minDate = minDate
            this.maxDate = maxDate
        }

        constructor(parcel: Parcel) {
            minDate = parcel.readLong()
            maxDate = parcel.readLong()
        }

        override fun isValid(date: Long): Boolean {
            return !(minDate > date || maxDate < date)
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeLong(minDate)
            dest.writeLong(maxDate)
        }

        companion object {
            val CREATOR: Parcelable.Creator<RangeValidator?> =
                object : Parcelable.Creator<RangeValidator?> {
                    override fun createFromParcel(parcel: Parcel): RangeValidator? {
                        return RangeValidator(parcel)
                    }

                    override fun newArray(size: Int): Array<RangeValidator?> {
                        return arrayOfNulls(size)
                    }
                }
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
                if (minute.toInt() < 10) {
                    ed_jam.text = "0$hour:0$minute"
                } else {
                    ed_jam.text = "0$hour:$minute"
                }

            } else {
                if (minute.toInt() < 10) {
                    ed_jam.text = "$hour:0$minute"
                } else {
                    ed_jam.text = "$hour:$minute"
                }
            }

        }
    }

    private fun writeDate() {
        FireBaseRepo().getDataBus(dw_id_bus.text.toString()).addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.toObject(BusDataClass::class.java)
                dw_id_bus.setText(data!!.id)
                dw_berangkat.setText(data!!.jurusan)
                ed_plat.setText(data.plat)
                ed_driver.setText(data.driver)
            }
        }
    }

    private fun getAllData() {
        var data = TicketPostDataClass()
        data.type = dd_tipe_bus.text.toString()
        if (data.type == "EKONOMI AC") {
            for (i in 0 until 40) {
                val nomor = i + 1
                data.position.add(i, (DataClassIsKosong(isKosong = true, nomor = nomor.toString())))
            }
        } else {
            for (i in 0 until 24) {
                val nomor = i + 1
                data.position.add(i, (DataClassIsKosong(isKosong = true, nomor = nomor.toString())))
            }
        }

        data.id = dw_id_bus.text.toString()
        data.harga = ed_harga.text.toString()
        data.terminal = dw_terminal.text.toString()
        data.nama = "${dw_berangkat.text.toString()}-${dw_tujuan.text.toString()}"
        data.noplat = ed_plat.text.toString()
        data.driver = ed_driver.text.toString()

        data.tanggal.addAll(list)
        Log.d(tag, " data tanggal ${data.tanggal}")
        data.pergi = ed_jam.text.toString()
        if (data.harga != "" && data.id != ""
            && data.terminal != "" && data.nama != "" && data.type != ""
            && data.tanggal.isNotEmpty() && data.position != mutableListOf<DataClassIsKosong>() && data.pergi !== "" && data.driver != "" && data.noplat != ""
        ) {
            GlobalScope.launch {
                try {
                    val data = async {
                        FireBaseRepo().PostTicket(data)
                        FireBaseRepo().postPosisiTanggal(data)
                        FireBaseRepo().PostBus(data)

                    }
                    data.await()
                } catch (e: Exception) {
                    Toast.makeText(this@AddTicketActivity, "permintaan gagal", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Toast.makeText(this@AddTicketActivity, "Mohon isi semua data", Toast.LENGTH_SHORT)
                .show()
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
                onBackPressed()

            }
        }
    }


}