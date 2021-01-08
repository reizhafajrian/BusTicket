package com.example.busticketactivity.regist

import android.Manifest
import android.app.Activity
import android.content.Context

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log

import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.busticketactivity.R
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.home.HomeActivity
import com.example.busticketactivity.utils.ExtraKey.Companion.FROM_CAMERA_CODE
import com.example.busticketactivity.utils.ExtraKey.Companion.FROM_GALLERY_CODE
import com.example.busticketactivity.utils.ExtraKey.Companion.PERMISSION_CODE
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_regist_add_image.*


class RegistAddImageActivity : AppCompatActivity(), View.OnClickListener {
    @Parcelize
    data class Data(
        var username: String? = "",
        var password: String? = "",
        var email: String? = "",
        var nama: String? = "",
        var imageLink: String = "",
        var bio: String = ""
    ) : Parcelable

    private lateinit var auth: FirebaseAuth
    private var imageCameraUri: Uri? = Uri.EMPTY
    private var resIdPhoto: Int? = null
    private var imageUri: Uri? = Uri.EMPTY
    private var storageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_add_image)
        initiateUi()
    }

    private fun initiateUi() {
        btn_add_image.setOnClickListener(this)
        btn_continue.setOnClickListener(this)
        btn_back_regis.setOnClickListener(this)
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

            } else {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, FROM_GALLERY_CODE, null)
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
        storageRef = FirebaseStorage.getInstance().reference.child("userImages")
        if (resultCode == Activity.RESULT_OK && getResIdPhoto() == R.id.iv_ava) {
            when (requestCode) {
                FROM_CAMERA_CODE -> {
                    iv_ava.setImageURI(getImageCameraUri())
                    btn_add_image.visibility = View.GONE
                    card_image.visibility = View.VISIBLE
                    imageUri = getImageCameraUri()

                }
                FROM_GALLERY_CODE -> {
                    iv_ava.setImageURI(data?.data)
                    btn_add_image.visibility = View.GONE
                    card_image.visibility = View.VISIBLE
                    imageUri = data?.data


                }
            }
        }

    }

    private fun uploadToDatabase(url: GetUrlListner) {
        if (imageUri != null) {
            val file = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
            val uploadTask: StorageTask<*>
            uploadTask = file.putFile(imageUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                if (!it.isSuccessful) {
                    it.exception?.let {
                        throw it
                    }
                }
                return@Continuation file.downloadUrl
            }).addOnCompleteListener {
                if (it.isSuccessful) {
                    val downloadUrl = it.result
                    val mUri = downloadUrl.toString()
                    url.getUrl(mUri)
                }
                else{
                    deleteUser()
                }
            }.addOnFailureListener {
                deleteUser()
            }

        }
    }

    private fun regist(dataUpdate: Data) {
        spinner.visibility = View.VISIBLE
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(
            dataUpdate.email.toString(),
            dataUpdate.password.toString()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uploadToDatabase(object : GetUrlListner {
                        override fun getUrl(url: String) {
                            dataUpdate.imageLink = url
                            FireBaseRepo().createUser(dataUpdate).addOnCompleteListener {
                                val emailPref =
                                    getSharedPreferences("email", Context.MODE_PRIVATE).edit()
                                emailPref.putString("email", dataUpdate.email).apply()
                                spinner.visibility = View.GONE
                                val intent =
                                    Intent(this@RegistAddImageActivity, HomeActivity::class.java)
                                startActivity(intent)
                            }
                                .addOnFailureListener {
                                    deleteUser()
                                }
                        }
                    })
                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    deleteUser()
                }
            }
            .addOnFailureListener {
            }
    }

    private fun deleteUser() {
        val auth = Firebase.auth.currentUser!!
        auth.delete().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("RegistAddImageActivity", "gagal")
            } else {
                deleteUser()
            }
        }
    }

    override fun onClick(v: View?) {
        val nama = findViewById<EditText>(R.id.ed_nama).text.toString()
        val bio = findViewById<EditText>(R.id.ed_bio).text.toString()
        val DataItem = intent.getParcelableExtra<Item>("itemregist")

        val dataUpdate = Data(
            username = DataItem?.Username,
            password = DataItem?.password,
            email = DataItem?.email,
            nama = nama,
            bio = bio,
            imageLink = ""
        )

        when (v?.id) {
            R.id.btn_back_regis -> {
                onBackPressed()
            }
            R.id.btn_add_image -> {
                getPictures(R.id.iv_ava)
            }
            R.id.iv_ava -> {
                getPictures(R.id.iv_ava)
            }
            R.id.btn_continue -> {


                if (nama == "" || bio == "" || imageUri!!.equals(Uri.EMPTY)) {
                    Toast.makeText(this, "Mohon masukan nama,bio dan foto anda", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "$dataUpdate", Toast.LENGTH_SHORT).show()
                    regist(dataUpdate)
                }
            }
        }
    }
}
