package com.example.busticketactivity.tiketmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.listener.ItemDetailListener
import com.example.busticketactivity.listener.TicketItemListener
import kotlinx.android.synthetic.main.item_detail_tiket.view.*


class ItemDetailTiketAdapter(private val list: MutableList<InfoTiket>,private val listener:ItemDetailListener) : RecyclerView.Adapter<ItemDetailTiketAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val cvItem=itemView.findViewById<CardView>(R.id.cv_item)
        fun bind(data: InfoTiket) {
            with(itemView){
                tv_berangkat.text=data.pergi
                tv_terminal.text=data.terminal
                tv_nomor.text=data.nomorKursi
                tv_type_bus.text=data.type
                tv_title_bus.text=data.namaBus
                tv_id.text=data.id
                val posisi=adapterPosition
                cvItem.setOnClickListener { listener.onItemClick(posisi,data) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.item_detail_tiket,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }

}
