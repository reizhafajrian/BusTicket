package com.example.busticketactivity.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.busticketactivity.R
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.dataclass.UserObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        loadData()
    }

    private fun loadData() {
        val email = getSharedPreferences("email", Context.MODE_PRIVATE)
        val dataEmail=email.getString("email","")
        FireBaseRepo().getUser(email = dataEmail!!).addOnCompleteListener {
            if (it.isSuccessful) {
                spinner.visibility=View.GONE
                val list = it.result!!.toObject(UserObject::class.java)
                Picasso.get()
                tv_nama_2.text = list?.nama
                tv_bio_2.text = list?.email
                if(list?.imageUrl!=""){
                    Picasso.get().load(list?.imageUrl).into(iv_ava)
                }
            }
        }
    }
}