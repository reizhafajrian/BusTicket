package com.example.busticketactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.firebase.DataClassIsKosong
import com.example.busticketactivity.listener.ListenerPickTicket
import kotlinx.android.synthetic.main.item_pick_ticket.view.*

class PickTicketAdapter(private val list: MutableList<DataClassIsKosong?>, var listener: ListenerPickTicket):RecyclerView.Adapter<PickTicketAdapter.ListViewHolder>(){
    private val TAG="PickTicketActivity"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_pick_ticket,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item =list[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(item:DataClassIsKosong){
            val hasil=item
              val color=hasil.isKosong


            with(itemView){
                tv_nomor.text=hasil.nomor

                if (color==false){
                    itemView.cd_available.setCardBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorRed))
                }
                else{
                    cd_available.setOnClickListener { listener.onClicks(hasil.nomor!!) }
                }

            }
        }
    }
}

