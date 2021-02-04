package com.example.busticketactivity.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.listener.TicketItemListener
import com.example.busticketactivity.pickticket.DataItemPickup
import kotlinx.android.synthetic.main.item_ticket.view.*


class ItemTicketAdapter(
    private val list: MutableList<DataItemPickup>,
    private val listener: TicketItemListener
) : RecyclerView.Adapter<ItemTicketAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = list[position]

        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: DataItemPickup) {
            with(itemView) {
                tv_id.text = item.id
                itemView.tv_title_bus.text = item.nama
                itemView.tv_type_bus.text = item.type
                itemView.tv_terminal.text = item.terminal
                itemView.tv_berangkat.text = "${item.pergi}"
                cd_item.setOnClickListener { listener.onItemClick(item.id) }
                val v=item.posisi.filter {
                    it!!.isKosong==false
                }
                if (v.size==item.posisi.size){
//                    tv_full.text="full"
                }

            }
        }

    }

}