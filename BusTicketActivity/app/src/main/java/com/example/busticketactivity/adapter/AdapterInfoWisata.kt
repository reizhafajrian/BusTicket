package com.example.busticketactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.utils.DataClassInfoWisata
import com.example.busticketactivity.utils.ListInfoWisata
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_info_wisata.view.*

class AdapterInfoWisata(val list: ListInfoWisata) : RecyclerView.Adapter<AdapterInfoWisata.ViewHolder>() {
    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        fun bind(data:DataClassInfoWisata){
            with(itemView){
                Picasso.get().load(data.img).into(image)
                text.text=data.text
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.item_info_wisata,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.list[position])
    }

    override fun getItemCount(): Int {
        return list.list.size
    }
}