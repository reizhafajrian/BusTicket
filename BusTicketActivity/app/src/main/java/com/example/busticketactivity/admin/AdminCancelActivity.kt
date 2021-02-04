package com.example.busticketactivity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.busticketactivity.R
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.utils.ListenerItemCancel
import kotlinx.android.synthetic.main.activity_admin_cancel.*

class AdminCancelActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    ListenerItemCancel {
    val tag = "AdminCancelActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_cancel)
        getData()
        refresh_layout.setOnRefreshListener(this)
        refresh_layout.post(object : Runnable {
            override fun run() {
                refresh_layout.isRefreshing = true
                getData()
            }

        })
    }


    private fun getData() {
        refresh_layout.isRefreshing = true
        FireBaseRepo().getCancel().addOnCompleteListener {
            if (it.isSuccessful) {
                var dataHasil = mutableListOf<String>()
                val data = it!!.result!!.documents
                if (data != null) {
                    for (i in data) {
                        dataHasil.add(i.id.toString())
                    }
                    AdminCancelAdapter(dataHasil,this).notifyDataSetChanged()
                    rv_admin_cancel.apply {
                        layoutManager = LinearLayoutManager(
                            this@AdminCancelActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        adapter = AdminCancelAdapter(dataHasil, this@AdminCancelActivity)
                    }

                }
                refresh_layout.isRefreshing = false
            } else {
                refresh_layout.isRefreshing = false
            }
        }.addOnCanceledListener {
            refresh_layout.isRefreshing = false
        }.addOnFailureListener {
            refresh_layout.isRefreshing = false
        }
    }

    override fun onRefresh() {
        getData()
    }

    override fun click(id: String) {
        if (id != "") {
            when (id) {
                id -> {
                    val intent = Intent(this, AdminDetailActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                }
            }
        }
    }

}