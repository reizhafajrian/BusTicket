package com.example.busticketactivity.regist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.busticketactivity.R
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), View.OnClickListener {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btn_continue.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        var namaEd=findViewById<EditText>(R.id.ed_username).text.toString()
        var passwordEd=findViewById<EditText>(R.id.ed_password).text.toString()
        var emailEd=findViewById<EditText>(R.id.ed_email).text.toString()
        val new=Item(Username = namaEd,password = passwordEd ,email = emailEd)
        val intent= Intent(this, RegistAddImageActivity::class.java)
        intent.putExtra("itemregist",new)
        when(v?.id){
            R.id.btn_continue->{
                startActivity(intent)
            }
        }
    }
}