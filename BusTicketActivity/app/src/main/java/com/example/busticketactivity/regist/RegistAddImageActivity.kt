package com.example.busticketactivity.regist

import android.Manifest
import android.app.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable

import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.busticketactivity.R
import com.example.busticketactivity.utils.ExtraKey
import com.example.busticketactivity.utils.ExtraKey.Companion.FROM_CAMERA_CODE
import com.example.busticketactivity.utils.ExtraKey.Companion.FROM_GALLERY_CODE
import com.example.busticketactivity.utils.ExtraKey.Companion.PERMISSION_CODE
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_regist_add_image.*


class RegistAddImageActivity : AppCompatActivity(), View.OnClickListener{
    @Parcelize
    data class Data(
        var username:String="",
        var password:String="",
        var email:String="",
        var nama:String="",
        var image:Uri?= Uri.EMPTY,
        var bio:String=""
        ):Parcelable
    private var imageCameraUri: Uri? = Uri.EMPTY
    private var resIdPhoto: Int? = null
    private var imageUri: Uri? = Uri.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_add_image)

        initiateUi()
    }
    private fun initiateUi(){
        btn_add_image.setOnClickListener(this)
        btn_continue.setOnClickListener(this)
        iv_ava.setOnClickListener(this)

    }
    //Function untuk mengambil gambar jika dari gallery
    private fun getImageCameraUri(): Uri? = imageCameraUri

    //Function untuk mengambil resid view yang sedang diklik
    //Fungsinya untuk mengetahui view mana yang akan dimasukkan gambarnya
    private fun getResIdPhoto(): Int? = resIdPhoto

    //Untuk mengambil gambar serta melakukan check permission untuk camera dan file manager
    //resId digunakan sebagai informasi view mana yang akan dimasukkan gambarnya
    private fun getPictures(resId: Int?) {
        resIdPhoto = resId
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                //permission was not enabled
                val permission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //show popup to request permission
                ActivityCompat.requestPermissions(
                    this,
                    permission,
                    PERMISSION_CODE
                )
                openGallery()
            }
        }
    }

    private fun openGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, ExtraKey.FROM_GALLERY_CODE, null)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    getPictures(getResIdPhoto())
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && getResIdPhoto() == R.id.iv_ava) {
            when (requestCode) {
                FROM_CAMERA_CODE -> {
                    iv_ava.setImageURI(getImageCameraUri())
                    btn_add_image.visibility=View.GONE
                    card_image.visibility=View.VISIBLE
                    imageUri = getImageCameraUri()
                }
                FROM_GALLERY_CODE -> {
                    iv_ava.setImageURI(data?.data)
                    btn_add_image.visibility=View.GONE
                    card_image.visibility=View.VISIBLE
                    imageUri = data?.data

                }
            }
        }

    }
    override fun onClick(v: View?) {
        var nama=findViewById<EditText>(R.id.ed_nama).text.toString()
        var bio=findViewById<EditText>(R.id.ed_bio).text.toString()
        val DataItem=intent.getParcelableExtra<Item>("itemregist")
        val dataUpdate=Data(username = DataItem.Username,password = DataItem.password,email = DataItem.email,nama = nama,bio = bio,image = imageUri)

        when(v?.id){
            R.id.btn_add_image->{
              getPictures(R.id.iv_ava)

            }
            R.id.iv_ava->{
                getPictures(R.id.iv_ava)
            }
            R.id.btn_continue->{
                Toast.makeText(this, "$dataUpdate", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
