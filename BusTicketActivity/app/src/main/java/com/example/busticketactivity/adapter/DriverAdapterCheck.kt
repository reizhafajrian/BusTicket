package com.example.busticketactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.listener.ListenerDriverCheck
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.item_histori.view.*

class DriverAdapterCheck(private val list: MutableList<String>,private val listener:ListenerDriverCheck) : RecyclerView.Adapter<DriverAdapterCheck.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(data: String){
            with(itemView){
                tv_email.text=data.toString()
                cv_item.setOnClickListener { listener.itemClick(data) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.item_histori,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}