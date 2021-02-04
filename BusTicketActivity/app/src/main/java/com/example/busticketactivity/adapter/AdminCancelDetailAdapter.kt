package com.example.busticketactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.dataclass.InfoTiket
import kotlinx.android.synthetic.main.item_cancel_detail_ticket.view.*

class AdminCancelDetailAdapter(private val data: ManagerGetData?) :
    RecyclerView.Adapter<AdminCancelDetailAdapter.ViewHolder>() {
    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(item: InfoTiket) {
            with(itemView) {
                tv_title_bus.text =item.nama
                tv_type_bus.text =item.type
                tv_nomor.text ="Nomor Kursi :${item.nomorKursi}"
                tv_berangkat.text =item.pergi
                tv_id.text=item.id
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cancel_detail_ticket, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data != null) {
            holder.bind(data.data[position])
        }

    }

    override fun getItemCount(): Int {
        return data!!.data.size
    }
}

