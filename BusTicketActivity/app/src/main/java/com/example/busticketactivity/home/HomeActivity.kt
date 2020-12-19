package com.example.busticketactivity.home


import android.content.Context
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ItemMenuAdapter
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.item.ItemMenuClass
import com.example.busticketactivity.listener.MenuItemListener
import com.example.busticketactivity.signin.UserObject
import com.example.busticketactivity.tiketmenu.TiketActivty
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), MenuItemListener {
    private lateinit var rvMenu: RecyclerView
    private val Tag="HomeActivity"
    private var listItem = mutableListOf<ItemMenuClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        intiateUI()
        getDataUser()
        showRecyleList()
    }



    private fun getDataUser(){
        val emailPref=getSharedPreferences("email",Context.MODE_PRIVATE)
        val email=emailPref.getString("email","")
        if (!email.isNullOrEmpty()){
        FireBaseRepo().getUser(email)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val list=it.result!!.toObject(UserObject::class.java)
                    val user=getSharedPreferences("dataUser",Context.MODE_PRIVATE).edit()
                    val data= Gson()//inisialisasi GSOn
                    user.putString("dataUser",data.toJson(list)).commit()//menset data user sebagai shared preferences
                    if (list != null) {

                        tv_name.text=list.nama
                    }
                 }
                else{
                    Toast.makeText(this, "data anda gagal di tampilkan mohon cek koneksi anda", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun intiateUI() {
        val list = mutableListOf(
            ItemMenuClass(R.drawable.ic_scan,"Scan"),
            ItemMenuClass(R.drawable.ic_shop,"Beli"),
            ItemMenuClass(R.drawable.ic_history,"History")
        )
        rvMenu = findViewById(R.id.rv_menu)
        rvMenu.setHasFixedSize(true)
        listItem=list


    }
    private fun showRecyleList() {
        rvMenu.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val listItemAdapter = ItemMenuAdapter(listItem,this)
        rvMenu.adapter = listItemAdapter
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onItemClick(nama: String) {
        when(nama){
           "Beli"->{
               val intent=Intent(this,TiketActivty::class.java)
               startActivity(intent)
            }
            "Scan"->{
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }
            "History"->{
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }
        }
    }
}