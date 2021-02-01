package com.example.busticketactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.busticketactivity.adapter.AdminCancelDetailAdapter
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.firebase.FireBaseRepo
import kotlinx.android.synthetic.main.activity_cancel_user.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class CancelUserActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {
    private var email:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_user)


        val data=getSharedPreferences("email", MODE_PRIVATE)
        val dataemail=data.getString("email","")
        email=dataemail.toString()
        tv_title.text=email
        getData()
        ly_refresh.setOnRefreshListener(this)
        ly_refresh.isRefreshing=true
        ly_refresh.post {
            ly_refresh.isRefreshing = true
            getData()
            ly_refresh.isRefreshing = false
        }

    }

    private fun getData()=GlobalScope.launch {
        try {
            val data=FireBaseRepo().GetCancelTicketUser(email).addOnCompleteListener {
                if(it.isSuccessful){
                    val GetCancel= it.result!!.toObject(ManagerGetData::class.java)
                    rv_ticket_cancel_detail.apply {
                        layoutManager=LinearLayoutManager(this@CancelUserActivity,RecyclerView.VERTICAL,false)
                        adapter=AdminCancelDetailAdapter(GetCancel)
                    }
                }
            }
            data.await()
        }catch (e:Exception){

        }

    }

    override fun onRefresh() {
        getData()
        ly_refresh.isRefreshing = false
    }
}