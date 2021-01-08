package com.example.busticketactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.signin.DataTIket
import com.example.busticketactivity.tiketmenu.InfoTiket
import kotlinx.android.synthetic.main.item_manager.view.*

class ManagerAdapter(private val list: ManagerGetData) :RecyclerView.Adapter<ManagerAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(data: InfoTiket){
        with(itemView){
           tv_email.text=data.email
           tv_harga.text=data.harga
        }
    }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.item_manager,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.data[position])
    }

    override fun getItemCount(): Int {
        return list.data.size
    }
}