package com.example.busticketactivity.home


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.CancelUserActivity
import com.example.busticketactivity.infowisata.DetailInfoWisataActivity
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ItemMenuAdapter
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.dataclass.ItemMenuClass
import com.example.busticketactivity.listener.MenuItemListener
import com.example.busticketactivity.dataclass.UserObject
import com.example.busticketactivity.tiketmenu.TiketActivty
import com.example.busticketactivity.tiketmenu.TiketDetailActivity
import com.example.busticketactivity.utils.DataClassInfoWisata
import com.example.busticketactivity.utils.ListInfoWisata
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.ImageClickListener
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), MenuItemListener, View.OnClickListener {
    private lateinit var rvMenu: RecyclerView
    private var listItem = intiateUI()
    val Tag = "HomeActivity"
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        carousel()
        getDataUser()
        showRecyclerList()
        iv_ava.setOnClickListener(this)
    }

    private fun carousel() {
        val dataclass0 = ListInfoWisata(
            list = mutableListOf(
                DataClassInfoWisata(
                    img = R.drawable.maliboro,
                    text = "Malioboro merupakan surganya pusat belanja dan oleh-oleh di kota Jogja selain itu di tempat ini merupakan tempat wisata kuliner di Jogja. Beraneka macam makanan khas Jogja tersedia lengkap di tempat ini selain itu juga banyak fasilitas tempat bermalam di daerah ini.Malioboro merupakan pusat keramaian kota di Jogja, kawasan ini semakin malam semakin ramai dikunjungi dengan kehangatan menjadikan suasananya unik yang khas di Jogja. Tempat Malioboro juga surganya wisata kuliner di Jogja di lokasi ini kita bisa dapatkan makanan khas juga oleh-oleh dengan mudah.\n"
                ),
                DataClassInfoWisata(
                    img = R.drawable.kidul,
                    text = "Alun-alun merupakan ciri khas identitas di tiap kota dan kota Jogja menawarkan pesonanya terutama di saat menjelang malam hari. Alun-alun selalu ramai banyak di padati oleh pengunjung di malam hari dengan kehadiran suasana yang hangat dan kita bisa menikmati kuliner yang di jajakan di sepanjang jalan. Lokasi Alun-alun Kidul terletak di Selatan kota Jogja tepatnya di Jl. Alun-alun Kidul, kita bisa menikmati suasana khas yang berbeda. Di tempat ini selalu dipadati pengunjung dari warga Jogja sampai luar daerah karena tempatnya yang klasik dan romantis"
                )
            )
        )
        val dataclass2 = ListInfoWisata(
            list = mutableListOf(
                DataClassInfoWisata(
                    img = R.drawable.kaliworo,
                    text = "Tempat wisata berikutnya adalah Cuug Winong Kaliwiro. Tempat ini memiliki akses jalan yang terbilang cukup sulit karena harus melalui pemukiman penduduk setempat. Tetapi Anda juga tak perlu takut tersesat karena di sepanjang perjalanan ini ada petunjuk jalan agar bisa sampai di lokasi wisata. Curug Winong Kaliwiro ini berada di Desa Winongsari Kecamatan Kaliwiro Wonosobo. Ketika nanti sudah sampai di tempat parkiran, Anda bisa melanjutkan perjalanan dengan jarak sekitar 300 meter untuk sampai ke lokasi wisata.\n"
                ),
                DataClassInfoWisata(
                    img = R.drawable.dieng,
                    text = "Yang tak kalah ndah adalah Kawah Dieng. Anda bisa mengunjungi tempat wisata ini dengan pemandangan alam yang sangat khas. Disebut juga dengan Kawah Sikidang yang termasuk sebagai tempat wisata di Wonosobo paling favorit. Bahkan banyak warga dari luar kota yang datang berkunjung. \n" +
                            "Wisata gunung api ini juga memiliki aktivitas vulkanik sehingga kalaupu datang harus berhati-hati. Luas Kawah Dieng sekitar 200 meter persegi dan letaknya berada di dataran yang cukup rendah. Letaknya berada di Bakal Buntu, Dieng Kulon.\n"
                )
            )
        )
        val dataclass1 = ListInfoWisata(
            list = mutableListOf(
                DataClassInfoWisata(
                    img = R.drawable.sewu,
                    text = "Candi Sewu atau Manjusrighra adalah candi Buddha yang dibangun pada abad ke-8 Masehi yang berjarak hanya delapan ratus meter di sebelah utara Candi Prambanan. Candi Sewu merupakan kompleks candi Buddha terbesar kedua setelah Candi Borobudur di Jawa Tengah"
                ),
                DataClassInfoWisata(
                    img = R.drawable.plaosan,
                    text = "Candi Plaosan adalah sebutan untuk kompleks percandian yang terletak di Dukuh Plaosan, Desa Bugisan, Kecamatan Prambanan, Kabupaten Klaten, Provinsi Jawa Tengah, Indonesia. Candi ini terletak kira-kira satu kilometer ke arah timur-laut dari Candi Sewu atau Candi Prambanan.\n"
                )
            )
        )

        val image = mutableListOf<Int>(
            R.drawable.bg_carousel_1, R.drawable.bg_carousel_2, R.drawable.bg_carousel_3
        )
        carouselView.pageCount = image.size
        carouselView.setImageListener(object : ImageListener {
            override fun setImageForPosition(position: Int, imageView: ImageView?) {
                imageView?.setImageResource(image[position])
            }
        })
        carouselView.setImageClickListener(object : ImageClickListener {
            override fun onClick(position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(this@HomeActivity, DetailInfoWisataActivity::class.java)
                        val gson = Gson()
                        val data = gson.toJson(dataclass0)
                        intent.putExtra("data", data)
                        intent.putExtra("title", "Wisata Jojga")
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this@HomeActivity, DetailInfoWisataActivity::class.java)
                        val gson = Gson()
                        val data = gson.toJson(dataclass1)
                        intent.putExtra("data", data)
                        intent.putExtra("title", "Wisata WonoSobo")
                        startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(this@HomeActivity, DetailInfoWisataActivity::class.java)
                        val gson = Gson()
                        val data = gson.toJson(dataclass2)
                        intent.putExtra("data", data)
                        intent.putExtra("title", "Wisata Klaten")
                        startActivity(intent)
                    }
                }

            }

        })


    }


    private fun getDataUser() {
        layout.visibility = View.VISIBLE
        val emailPref = getSharedPreferences("email", Context.MODE_PRIVATE)
        val email = emailPref.getString("email", "")
        if (!email.isNullOrEmpty()) {
            FireBaseRepo().getUser(email)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        layout.visibility = View.GONE
                        val list = it.result!!.toObject(UserObject::class.java)
                        val prefs = getSharedPreferences("dataUser", Context.MODE_PRIVATE).edit()
                        val dataUser = gson.toJson(list)
                        prefs.putString("dataUser", dataUser).apply()
                        if (list != null) {
                            tv_name.text = list.nama
                            if (!(list.imageUrl).equals("")) {
                                Picasso.get().load(list?.imageUrl).into(iv_ava)
                            }
                        }
                    } else {
                        layout.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "data anda gagal di tampilkan mohon cek koneksi anda",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun intiateUI(): MutableList<ItemMenuClass> {
        val list = mutableListOf(
            ItemMenuClass(R.drawable.ic_scan, "Scan"),
            ItemMenuClass(R.drawable.ic_shop, "Beli"),
            ItemMenuClass(R.drawable.ic_shop, "Cancel"),
            ItemMenuClass(R.drawable.ic_info, "info"),
            ItemMenuClass(R.drawable.ic_logout, "SignOut")
        )
        return list

    }

    private fun showRecyclerList() {
        rvMenu = findViewById(R.id.rv_menu)
        rvMenu.apply {
            setHasFixedSize(true)
            layoutManager =
                GridLayoutManager(this@HomeActivity,4, LinearLayoutManager.VERTICAL, false)
            adapter = ItemMenuAdapter(listItem, this@HomeActivity)
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
//                FireBaseRepo().posisiByTanggal()
                val intent = Intent(this, TiketActivty::class.java)
                startActivity(intent)

            }
            "Scan" -> {
                val intent = Intent(this, TiketDetailActivity::class.java)
                startActivity(intent)
            }
            "SignOut" -> {
                val prefs = getSharedPreferences("login", MODE_PRIVATE).edit()
                prefs.putString("login", "")
                prefs.apply()
                finish()
            }
            "Cancel" -> {
                val intent=Intent(this@HomeActivity, CancelUserActivity::class.java)
                startActivity(intent)
            }
            "info" -> {
                val intent=Intent(this@HomeActivity,InfoActivity::class.java)
                startActivity(intent)

            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_ava -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }


}

