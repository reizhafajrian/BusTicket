package com.example.busticketactivity.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.busticketactivity.R
import com.example.busticketactivity.home.HomeActivity
import com.example.busticketactivity.regist.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        btn_sign_in.setOnClickListener(this)
        tv_register.setOnClickListener(this)


    }



    override fun onStart() {
        val prefs=getSharedPreferences("login",MODE_PRIVATE)
        if (!prefs.getString("login","").isNullOrEmpty()){
            Toast.makeText(this, "${prefs.getString("login","")}", Toast.LENGTH_SHORT).show()
            val intent=Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }
        super.onStart()
    }

    private fun firebaseLogin() {
        val prefs=getSharedPreferences("login",MODE_PRIVATE)
        val username = findViewById<EditText>(R.id.ed_username)
        val password =  findViewById<EditText>(R.id.ed_password)
        val usernameText=username.text.toString()
        val passwordText=password.text.toString()
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(usernameText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    prefs.edit().putString("login","${auth.currentUser}").apply()
                   val intent=Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "login gagal", Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }

    override fun onClick(v: View?) {
        val intent= Intent(this,
            RegisterActivity::class.java)


        when(v?.id){
            R.id.btn_sign_in ->{
                firebaseLogin()
            }
            R.id.tv_register ->{
                startActivity(intent)
            }
        }
    }
}