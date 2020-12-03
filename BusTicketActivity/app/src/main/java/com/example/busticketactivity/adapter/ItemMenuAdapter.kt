package com.example.busticketactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.item.ItemMenuClass
import com.example.busticketactivity.listener.MenuItemListener
import kotlinx.android.synthetic.main.item_menu.view.*


class ItemMenuAdapter(private val listItemData: MutableList<ItemMenuClass>,
                      private val listener: MenuItemListener) :
    RecyclerView.Adapter<ItemMenuAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       val item = listItemData[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
       return listItemData.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:ItemMenuClass){
            with(itemView){
                btn_rv.background=ContextCompat.getDrawable(itemView.context,item.imageButton)
                btn_rv.setOnClickListener { listener.onItemClick(item.nama) }
            }
        }

    }

}