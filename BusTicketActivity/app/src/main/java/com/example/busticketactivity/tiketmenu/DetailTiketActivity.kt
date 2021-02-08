package com.example.busticketactivity.tiketmenu

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import com.example.busticketactivity.R
import com.example.busticketactivity.admin.getName
import com.example.busticketactivity.dataclass.InfoTiket
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.firebase.FireBaseRepo
import com.google.gson.Gson
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.activity_detail_driver_t_icket.*
import kotlinx.android.synthetic.main.activity_detail_tiket.*
import kotlinx.android.synthetic.main.activity_detail_tiket.btn_cancel
import kotlinx.android.synthetic.main.activity_detail_tiket.tv_jam_berangkat
import kotlinx.android.synthetic.main.activity_detail_tiket.tv_nama_text
import kotlinx.android.synthetic.main.activity_detail_tiket.tv_nomor_kursi
import kotlinx.android.synthetic.main.activity_detail_tiket.tv_tanggal_pesan_berangkat
import kotlinx.android.synthetic.main.activity_detail_tiket.tv_tanggal_text
import kotlinx.android.synthetic.main.activity_detail_tiket.tv_terminal_text
import kotlinx.android.synthetic.main.activity_detail_tiket.tv_tipe_bus_text
import kotlinx.android.synthetic.main.activity_detail_tiket.tv_title
import java.text.SimpleDateFormat
import java.util.*


class DetailTiketActivity : AppCompatActivity(), View.OnClickListener {
    val gson=Gson()
    private var nama=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tiket)
        getData()
        initiateUi()
        btn_cancel.setOnClickListener(this)
    }

    private fun initiateUi() {
        val infoTiket = intent.getSerializableExtra("dataTiketPembayaran") as InfoTiket
        tv_jam_berangkat.text = infoTiket.pergi
        tv_nama_text.text = infoTiket.nama
        tv_terminal_text.text = infoTiket.terminal
        tv_nomor_kursi.text = infoTiket.nomorKursi
        tv_tanggal_pesan_berangkat.text=infoTiket.tanggalBeli
        tv_tanggal_text.text=infoTiket.tanggal
        tv_tipe_bus_text.text = infoTiket.type
        FireBaseRepo().getUserNama(infoTiket.email).addOnCompleteListener {
            if(it.isSuccessful){
                val hasil=it.result!!.toObjects(getName::class.java)
                nama=hasil[0].nama
                tv_title.text=hasil!![0].nama
            }
        }
    }

    private fun getData() {
        val infoTiket = intent.getSerializableExtra("dataTiketPembayaran") as InfoTiket
        infoTiket.namaUser=nama
        val gsontojson=gson.toJson(infoTiket,infoTiket::class.java)
        gsontojson
        val qrCode = QRGEncoder(gsontojson, null, QRGContents.Type.TEXT, 250)
        qrCode.apply {
            colorBlack = Color.BLACK
            colorWhite = Color.WHITE
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
        when (v?.id) {
            R.id.btn_cancel -> {
                cancelTicket()
            }
        }
    }

    private fun cancelTicket() {


        val infoTiket = intent.getSerializableExtra("dataTiketPembayaran") as InfoTiket
        val time = infoTiket.pergi.take(5)
        val datePergi = infoTiket.pergi.takeLast(11)
        val sdf = SimpleDateFormat("HHmm", Locale.getDefault())
        val date = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
        val dated = date.format(Date()).toString()
        val currentDateandTime: String = sdf.format(Date())
        val timeremove = time.replace(":", "")
        if (dated == infoTiket.tanggal) {
            if (timeremove.toInt() > currentDateandTime.toInt()) {
                val waktusisa = timeremove.toInt()
                if ((waktusisa - 100) >= currentDateandTime.toInt()) {
                    FireBaseRepo().canceltiket(infoTiket.id,infoTiket.tanggal, infoTiket.nomorKursi)
                    FireBaseRepo().deletetiket(infoTiket.email).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val list = it.result!!.toObject(ManagerGetData::class.java)
                            val hasul = list?.data
                            if (hasul != null) {
                                val data = hasul.filter { tiket ->
                                    tiket.nomorKursi != infoTiket.nomorKursi
                                }
                                val dataDelete = hasul.filter { tiket ->
                                    tiket.nomorKursi == infoTiket.nomorKursi
                                }
                                FireBaseRepo().repostTiket(infoTiket.email, data)
                                FireBaseRepo().postCancel(infoTiket.email, dataDelete[0])
                                Toast.makeText(
                                    this,
                                    "Pembatalan anda berhasil di proses",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                    else {
                    Toast.makeText(
                        this,
                        "Waktu untuk pembatalan kurang dari 1 jam",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            } else {
                Toast.makeText(this, "Waktu untuk pembatalan kurang dari 1 jam", Toast.LENGTH_SHORT)
                    .show()
                Toast.makeText(this, "Waktu untuk pembatalan kurang dari 1 jam", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(
                this,
                "Tanggal berbeda dengan keberangakatan tiket tidak bisa di cancel",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}