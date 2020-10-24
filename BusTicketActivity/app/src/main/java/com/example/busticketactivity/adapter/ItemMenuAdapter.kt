package com.example.busticketactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.item.ItemMenuClass
import com.google.android.material.button.MaterialButton

class ItemMenuAdapter(private val listItemData: ItemMenuClass) :
    RecyclerView.Adapter<ItemMenuAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listItemData.imageButton[position]
        holder.btnImage?.backgroundTintList=ContextCompat.getColorStateList(holder.itemView.context,item)
    }

    override fun getItemCount(): Int {
        return listItemData.imageButton.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnImage: MaterialButton? = itemView.findViewById<MaterialButton>(R.id.btn_rv)

    }

}