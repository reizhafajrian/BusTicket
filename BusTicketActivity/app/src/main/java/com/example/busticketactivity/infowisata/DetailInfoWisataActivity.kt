package com.example.busticketactivity.infowisata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.AdapterInfoWisata
import com.example.busticketactivity.utils.ListInfoWisata
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_info_wisata.*

class DetailInfoWisataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_info_wisata)
        showlist()

    }

    private fun showlist() {
        val data=intent.getStringExtra("data")
        val title=intent.getStringExtra("title")
        val hasil=Gson().fromJson(data, ListInfoWisata::class.java)
        tv_title.text=title
        rv_wisata.apply {
            layoutManager=LinearLayoutManager(this@DetailInfoWisataActivity,RecyclerView.VERTICAL,false)
            adapter= AdapterInfoWisata(hasil)
        }
    }

}