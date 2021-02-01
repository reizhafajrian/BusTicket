package com.example.busticketactivity.signin

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.busticketactivity.DetailDriverTIcketActivity
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.tiketmenu.InfoTiket
import com.google.gson.Gson
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_open_camera.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.dm7.barcodescanner.zxing.ZXingScannerView

class OpenCameraActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_camera)

        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    zxscan.setResultHandler(this@OpenCameraActivity)
                    zxscan.startCamera()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@OpenCameraActivity,
                        "you should enable premission",
                        Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }

            }).check()
    }

    override fun handleResult(rawResult: Result?) {
        val data = gson.fromJson(rawResult.toString(), InfoTiket::class.java)
        try {
            FireBaseRepo().CheckTicket(data).addOnCompleteListener {
                if (it.isSuccessful) {
                    try {
                        val dataHasil =
                            it.result!!.documents[0].toObject(ManagerGetData::class.java)
                        if (dataHasil!!.data.isNotEmpty()) {
                            val filter = dataHasil!!.data.filter {
                                it.id == data.id && it.nomorKursi == data.nomorKursi && it.namaBus == data.namaBus && it.tanggal == data.tanggal
                            }
                            val intent = Intent(this, DetailDriverTIcketActivity::class.java)
                            intent.putExtra("data", filter[0])
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, NotValidActivity::class.java)
                            startActivity(intent)
                        }
                    }catch (e:Exception){
                        val intent = Intent(this, NotValidActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
//        if(rawResult.toString().equals("settlement")){
//            val fragment= fragmentAproove()
//            activity?.supportFragmentManager?.beginTransaction()!!.apply {
//                replace(R.id.fragment,fragment)
//                addToBackStack("d")
//                commit()
//            }
//        }
//        else{
//
//            Log.d("DriverActivity","result $rawResult")
////          val fragment=FragmentDeny()
////          activity?.supportFragmentManager?.beginTransaction()!!.apply {
////              add(R.id.fragment,fragment)
////              addToBackStack(null)
////              commit()
//        }
        } catch (e: Exception) {
            val intent = Intent(this, NotValidActivity::class.java)
            startActivity(intent)
        }
    }
}