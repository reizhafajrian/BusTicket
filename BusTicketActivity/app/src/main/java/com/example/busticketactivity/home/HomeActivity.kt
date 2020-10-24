package com.example.busticketactivity.home


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.ItemMenuAdapter
import com.example.busticketactivity.item.ItemMenuClass
import kotlin.system.exitProcess

class HomeActivity : AppCompatActivity() {
    private lateinit var rvMenu: RecyclerView
    private var listItem =ItemMenuClass(imageButton = mutableListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        intiateUI()
        showRecyleList()
    }

    private fun intiateUI() {
        val list=ItemMenuClass(imageButton = mutableListOf(R.drawable.icon_pagoda))
        rvMenu = findViewById(R.id.rv_menu)
        rvMenu.setHasFixedSize(true)
        listItem=list
    }

    private fun showRecyleList(){
        rvMenu.layoutManager=LinearLayoutManager(this)
        val listItemAdapter=ItemMenuAdapter(listItem)
        rvMenu.adapter=listItemAdapter
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}