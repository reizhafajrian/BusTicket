package com.example.busticketactivity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.AdminCancelDetailAdapter
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.firebase.FireBaseRepo
import kotlinx.android.synthetic.main.activity_admin_detail.*
import kotlinx.android.synthetic.main.activity_admin_detail.tv_title


class AdminDetailActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private var id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_detail)
        getdata()
        ly_refresh.setOnRefreshListener(this)
        ly_refresh.post(object : Runnable {
            override fun run() {
                ly_refresh.isRefreshing = true
                ly_refresh
                getdata()
            }

        })
    }

    private fun getdata() {
        ly_refresh.isRefreshing = true
        id = intent.getStringExtra("id")
        tv_title.text = "Data $id"
        FireBaseRepo().cancelDetail(id!!).addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.toObject(ManagerGetData::class.java)
                rv_ticket_cancel_detail.apply {
                    layoutManager =
                        LinearLayoutManager(this@AdminDetailActivity, RecyclerView.VERTICAL, false)
                    adapter = AdminCancelDetailAdapter(data)
                }
                ly_refresh.isRefreshing = false
            } else {
                ly_refresh.isRefreshing = false
            }
        }.addOnCanceledListener {
            ly_refresh.isRefreshing = false
        }.addOnFailureListener {
            ly_refresh.isRefreshing = false
        }
    }

    override fun onRefresh() {
        getdata()
    }
}