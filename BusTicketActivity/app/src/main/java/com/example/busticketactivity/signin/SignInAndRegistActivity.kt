package com.example.busticketactivity.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.busticketactivity.R
import com.example.busticketactivity.regist.RegistAddImageActivity
import com.example.busticketactivity.regist.RegisterActivity
import kotlinx.android.synthetic.main.activity_sign_in_and_regist.*

class SignInAndRegistActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_and_regist)
        btn_sign_in.setOnClickListener(this)
        btn_register.setOnClickListener(this)
        initateUi()

    }

    private fun initateUi() {
        val prefs = getSharedPreferences("login", MODE_PRIVATE)
        val prefsmanager = getSharedPreferences("manager", MODE_PRIVATE)
        val prefskenek = getSharedPreferences("kenek", MODE_PRIVATE)
        val prefsadmin = getSharedPreferences("admin", MODE_PRIVATE)
        if (!prefs.getString("login", "").isNullOrEmpty()
            || !prefsmanager.getString("manager", "")
                .isNullOrEmpty() || !prefskenek.getString("kenek", "").isNullOrEmpty()
            || !prefsadmin.getString("admin", "").isNullOrEmpty()
        ) {
            val intent = Intent(
                this,
                SignInActivity::class.java
            )
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_sign_in -> {
                val intent = Intent(
                    this,
                    SignInActivity::class.java
                )
                startActivity(intent)
            }
            R.id.btn_register -> {
                val intent = Intent(
                    this,
                    RegistAddImageActivity::class.java
                )
                startActivity(intent)

            }
        }
    }
}