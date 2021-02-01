package com.example.busticketactivity.signin

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.busticketactivity.R
import com.example.busticketactivity.fragment.FragmentDeny
import com.example.busticketactivity.fragment.fragmentAproove
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_open_camera.*
import me.dm7.barcodescanner.zxing.ZXingScannerView


class OpenCameraFragment : Fragment(), ZXingScannerView.ResultHandler {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_open_camera, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      Dexter.withActivity(activity)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    zxscan.setResultHandler(this@OpenCameraFragment)
                    zxscan.startCamera()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(activity, "you should enable premission", Toast.LENGTH_SHORT).show()
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
      if(rawResult.toString().equals("settlement")){
          val fragment=fragmentAproove()
          activity?.supportFragmentManager?.beginTransaction()!!.apply {
              replace(R.id.fragment,fragment)
              addToBackStack("d")
              commit()
          }
      }
        else{

            Log.d("DriverActivity","result $rawResult")
//          val fragment=FragmentDeny()
//          activity?.supportFragmentManager?.beginTransaction()!!.apply {
//              add(R.id.fragment,fragment)
//              addToBackStack(null)
//              commit()
          }
        }
    }
