package com.example.busticketactivity.admin

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.TicketPostDataClass
import com.example.busticketactivity.firebase.DataClassIsKosong
import com.example.busticketactivity.firebase.FireBaseRepo
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_add_ticket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AddTicketActivity : AppCompatActivity(), View.OnClickListener {
    val tag = "AddTicketActivity"
    private var list= mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ticket)
        list()
        btn_simpan.setOnClickListener(this)
        ly_tanggal.setOnClickListener(this)
        ly_jam.setOnClickListener(this)
        driver()

    }

    private fun list() {
        val items = mutableListOf<Int>()
        for (i in 1 until 40) {
            items.add(i)
        }
        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
        dropdown.setAdapter(adapter)

    }

    private fun driver() {
        val items = mutableListOf<String>()
        FireBaseRepo().getDriver().addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.toObjects(GetDriverObject::class.java)
                for (i in data) {
                    items.add(i.nama)
                }
                val adapter = ArrayAdapter(this, R.layout.dropdown, items)
                ed_driver.setAdapter(adapter)
            }
        }
    }

    private fun date() {
        list.removeAll(list)
        val builder: MaterialDatePicker.Builder<Pair<Long, Long>> =
            MaterialDatePicker.Builder.dateRangePicker()
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
        builder.setCalendarConstraints(limitrange().build())
        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())

        picker.addOnPositiveButtonClickListener{
            ed_tanggal.text=picker.headerText
            val startDate= it.first
            val endDate = it.second
            val msDiff = endDate?.minus(startDate!!)
            val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff!!)
            val currentTime = SimpleDateFormat("dd-M-yyyy", Locale.getDefault()).format(Date())
            val sdf = SimpleDateFormat("dd-M-yyyy")
            val c=Calendar.getInstance()
            try {
                c.time = sdf.parse(currentTime)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            for(i in 0..daysDiff.toInt()){
                val data=sdf.format(c.time)
                list.add(i,data)
                Log.d(tag, "datanya ${data}")
                c.add(Calendar.DATE,1)
            }
            for (i in list){
                Log.d(tag,"ini tanggan ${i}")
            }
            Log.d(tag,"ini list $list")
        }
    }

    private fun limitrange(): CalendarConstraints.Builder {
        val calender=CalendarConstraints.Builder()
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
        val startDate = day.toInt()+1

        val endMonth = month.toInt()+1
        val endDate = day.toInt()+5
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
                    ed_jam.text = "0$hour:$minute"
                }
            }

        }
    }

    private fun getAllData() {
        var data = TicketPostDataClass()
        val isKosong = dropdown.text.toString()

        if (isKosong != "") {
            for (i in 0 until isKosong.toInt()) {
                val nomor = i + 1
                data.position.add(i, (DataClassIsKosong(isKosong = true, nomor = nomor.toString())))
            }
        } else {
            Toast.makeText(this@AddTicketActivity, "Mohon isi nomor kursi", Toast.LENGTH_SHORT)
                .show()
        }
        data.id = ed_id_bus.text.toString()
        data.harga = ed_harga.text.toString()
        data.terminal = ed_terminal.text.toString()
        data.nama = ed_tujuan.text.toString()
        data.noplat = ed_plat.text.toString()
        data.driver = ed_driver.text.toString()
        data.type = ed_type.text.toString()
        data.tanggal.addAll(list)
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