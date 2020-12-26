package com.example.busticketactivity.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.listener.TicketItemListener
import com.example.busticketactivity.tiketmenu.ItemDataTiket
import kotlinx.android.synthetic.main.item_ticket.view.*


class ItemTicketAdapter(private val list: MutableList<ItemDataTiket>,
                        private  val listener : TicketItemListener
                        ):RecyclerView.Adapter<ItemTicketAdapter.ListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_ticket,parent,false)
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
        fun bind(item: ItemDataTiket){
            with(itemView){
               itemView.tv_title_bus.text=item.nama
                itemView.tv_type_bus.text=item.type
                itemView.tv_terminal.text=item.terminal
                itemView.tv_berangkat.text=item.pergi
                cd_item.setOnClickListener { listener.onItemClick(item.nama) }
            }
        }

    }

}