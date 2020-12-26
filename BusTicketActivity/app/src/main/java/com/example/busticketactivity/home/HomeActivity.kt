package com.example.busticketactivity.home


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ItemMenuAdapter
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.dataclass.ItemMenuClass
import com.example.busticketactivity.listener.MenuItemListener
import com.example.busticketactivity.signin.UserObject
import com.example.busticketactivity.tiketmenu.TiketActivty
import com.example.busticketactivity.tiketmenu.TiketDetailActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), MenuItemListener {
    private lateinit var rvMenu: RecyclerView
    private var listItem = intiateUI()
    val Tag="HomeActivity"
    private val gson= Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        carousel()
        getDataUser()
        showRecyclerList()
    }
    private fun carousel(){
        val image= mutableListOf<Int>(
            R.drawable.ic_coursel_1,  R.drawable.ic_coursel_2,  R.drawable.ic_coursel_3
        )
        carouselView.pageCount=image.size
        carouselView.setImageListener(object: ImageListener{
            override fun setImageForPosition(position: Int, imageView: ImageView?) {
                imageView?.setImageResource(image[position])
            }
        })



    }



    private fun getDataUser() {
        layout.visibility= View.VISIBLE
        val emailPref = getSharedPreferences("email", Context.MODE_PRIVATE)
        val email = emailPref.getString("email", "")
        if (!email.isNullOrEmpty()) {
            FireBaseRepo().getUser(email)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        layout.visibility= View.GONE
                        val list = it.result!!.toObject(UserObject::class.java)
                        val prefs=getSharedPreferences("dataUser",Context.MODE_PRIVATE).edit()
                        val dataUser=gson.toJson(list)
                        prefs.putString("dataUser",dataUser).apply()
                        if (list != null) {
                            tv_name.text = list.nama
                            if (!(list.imageUrl).equals("")) {
                                Picasso.get().load(list?.imageUrl).into(iv_ava)
                            }
                        }
                    } else {
                        layout.visibility= View.GONE
                        Toast.makeText(
                            this,
                            "data anda gagal di tampilkan mohon cek koneksi anda",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun intiateUI():MutableList<ItemMenuClass> {
        val list = mutableListOf(
            ItemMenuClass(R.drawable.ic_scan, "Scan"),
            ItemMenuClass(R.drawable.ic_shop, "Beli"),
            ItemMenuClass(R.drawable.ic_logout, "SignOut")
        )
        return list

    }

    private fun showRecyclerList() {
        rvMenu = findViewById(R.id.rv_menu)
        rvMenu.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ItemMenuAdapter(listItem,this@HomeActivity)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    override fun onItemClick(nama: String) {
        when (nama) {
            "Beli" -> {
                val intent = Intent(this, TiketActivty::class.java)
                startActivity(intent)
            }
            "Scan" -> {
                val intent = Intent(this, TiketDetailActivity::class.java)
                startActivity(intent)
            }
            "SignOut" -> {
                val prefs=getSharedPreferences("login",MODE_PRIVATE).edit()
                prefs.putString("login","")
                prefs.apply()
                finish()
            }
        }
    }
}

