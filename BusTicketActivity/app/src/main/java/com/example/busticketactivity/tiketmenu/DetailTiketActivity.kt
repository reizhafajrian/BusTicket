package com.example.busticketactivity.tiketmenu

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import com.example.busticketactivity.R
import com.google.zxing.WriterException

import kotlinx.android.synthetic.main.activity_detail_tiket.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class DetailTiketActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tiket)
        getData()
        initiateUi()
        btn_cancel.setOnClickListener(this)
    }

    private fun initiateUi() {
         val infoTiket=intent.getSerializableExtra("dataTiketPembayaran") as InfoTiket
        tv_berangkat.text=infoTiket.pergi
        tv_title_bus.text=infoTiket.namaBus
        tv_terminal.text=infoTiket.terminal
        tv_nomor.text=infoTiket.nomorKursi
        tv_type_bus.text=infoTiket.type
    }

    private fun getData(){

        val qrCode=QRGEncoder("settlement", null, QRGContents.Type.TEXT, 250)
        qrCode.apply {
            colorBlack=Color.BLACK
            colorWhite=Color.WHITE
        }
        try {
            // Getting QR-Code as Bitmap
                val bitmap = qrCode.bitmap
            // Setting Bitmap to ImageView
            iv_barcode.setImageBitmap(bitmap)
        } catch (e: WriterException) {

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_cancel->{
                cancelTicket()
            }
        }
    }

    private fun cancelTicket() {
        val sdf = SimpleDateFormat("HHmm", Locale.getDefault())
        val currentDateandTime: String = sdf.format(Date())
        val date=Calendar.getInstance().time
        Log.d("DetailTiketActivity","ini time ${currentDateandTime}")

        if(currentDateandTime.toInt()<745){
            Toast.makeText(this, "berhasil di batalkan", Toast.LENGTH_SHORT).show()
        }
    }
}