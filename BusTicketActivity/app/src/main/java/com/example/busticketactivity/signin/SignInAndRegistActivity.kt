package com.example.busticketactivity.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.busticketactivity.R
import kotlinx.android.synthetic.main.activity_sign_in_and_regist.*

class SignInAndRegistActivity : AppCompatActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_and_regist)
        btn_sign_in.setOnClickListener(this)
        btn_register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_sign_in ->{
                val intent= Intent(this,
                    SignInActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_register ->{

            }
        }
    }
}