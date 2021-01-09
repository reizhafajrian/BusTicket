package com.example.busticketactivity.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.busticketactivity.AdminActivity
import com.example.busticketactivity.R
import com.example.busticketactivity.home.HomeActivity
import com.example.busticketactivity.regist.RegistAddImageActivity
import com.example.busticketactivity.regist.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        btn_sign_in.setOnClickListener(this)
        tv_register.setOnClickListener(this)
    }

    override fun onStart() {
        val prefs = getSharedPreferences("login", MODE_PRIVATE)
        val prefsmanager = getSharedPreferences("manager", MODE_PRIVATE)
        val prefskenek = getSharedPreferences("kenek", MODE_PRIVATE)
        val prefsadmin = getSharedPreferences("admin", MODE_PRIVATE)
        if (!prefs.getString("login", "").isNullOrEmpty()) {
            Toast.makeText(this, "${prefs.getString("login", "")}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else if (!prefsmanager.getString("manager", "").isNullOrEmpty()) {
            Toast.makeText(this, "${prefsmanager.getString("manager", "")}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ManagerActivity::class.java)
            startActivity(intent)
        } else if (!prefskenek.getString("kenek", "").isNullOrEmpty()) {
            Toast.makeText(this, "${prefskenek.getString("kenek", "")}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DriverActivity::class.java)
            startActivity(intent)
        }else if(!prefsadmin.getString("admin", "").isNullOrEmpty()){
            Toast.makeText(this, "${prefskenek.getString("kenek", "")}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        super.onStart()
    }

    private fun firebaseLogin() {
        spinner.visibility=View.VISIBLE
        val email = findViewById<EditText>(R.id.ed_username)
        val password = findViewById<EditText>(R.id.ed_password)
        val usernameText = email.text.toString()
        val passwordText = password.text.toString()

        //mendapatakan email dan menyimpannya
        val prefs = getSharedPreferences("login", MODE_PRIVATE)
        val prefsmanager = getSharedPreferences("manager", MODE_PRIVATE)
        val prefskenek = getSharedPreferences("kenek", MODE_PRIVATE)
        val emailPref = getSharedPreferences("email", Context.MODE_PRIVATE).edit()
        val prefsadmin = getSharedPreferences("admin", MODE_PRIVATE)
        emailPref.putString("email", usernameText).apply()


        auth = Firebase.auth
        auth.signInWithEmailAndPassword(usernameText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    spinner.visibility=View.GONE
                    when (usernameText) {
                        "ptbustiket@gmail.com" -> {
                            prefsmanager.edit().putString("manager", "${auth.currentUser}").apply()
                            val intent = Intent(this, ManagerActivity::class.java)
                            startActivity(intent)
                        }
                        "kenektiket@gmail.com" -> {
                            prefskenek.edit().putString("kenek", "${auth.currentUser}").apply()
                            val intent = Intent(this, DriverActivity::class.java)
                            startActivity(intent)
                        }
                        "admin@gmail.com"->{
                            prefsadmin.edit().putString("admin", "${auth.currentUser}").apply()
                            val intent = Intent(this, AdminActivity::class.java)
                            startActivity(intent)
                        }
                        else -> {
                            prefs.edit().putString("login", "${auth.currentUser}").apply()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }
                    }
                } else {
                    spinner.visibility=View.GONE
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "login gagal", Toast.LENGTH_SHORT).show()
                }
            }

//
    }

    override fun onClick(v: View?) {
        val intent = Intent(
            this,
            RegistAddImageActivity::class.java
        )
        when (v?.id) {
            R.id.btn_sign_in -> {
                if(ed_username.text.toString() !=""||ed_password.text.toString() !=""){
                    firebaseLogin()
                }
            }
            R.id.tv_register -> {
                startActivity(intent)
            }
        }
    }
}