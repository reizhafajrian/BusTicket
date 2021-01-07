package com.example.busticketactivity.regist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.busticketactivity.R
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity(), View.OnClickListener {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btn_continue.setOnClickListener(this)
        btn_back_regis.setOnClickListener(this)

    }
    private fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9]).{4,}$"
            val passwordMatcher = Regex(passwordPattern)
            return passwordMatcher.find(password) != null
        } ?: return false
    }
    private fun isValidEmail(Email: String?) : Boolean {
        Email?.let {
            val emailPattern = Patterns.EMAIL_ADDRESS

            return  emailPattern.matcher(Email).matches()
        } ?: return false
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
                if(namaEd==""||passwordEd==""||emailEd==""){
                    Toast.makeText(this, "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                }
                else if(!isValidPassword(passwordEd)||!isValidEmail(emailEd)){
                    Toast.makeText(this, "mohon masukan password yang berisi angka dan email yang valid", Toast.LENGTH_SHORT).show()
                }
                else{
                    startActivity(intent)
                }
            }
            R.id.btn_register->{
                onBackPressed()
            }
        }
    }
}