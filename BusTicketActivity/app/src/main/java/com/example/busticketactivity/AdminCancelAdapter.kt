package com.example.busticketactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.utils.ListenerItemCancel
import kotlinx.android.synthetic.main.item_cancel_admin.view.*

class AdminCancelAdapter(private val list:MutableList<String>,private val listener:ListenerItemCancel) : RecyclerView.Adapter<AdminCancelAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.item_cancel_admin,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(id:String){
            with(itemView){
                tv_email.text=id.toString()
                cv_item.setOnClickListener{
                    listener.click(id)
                }
            }
        }
    }


}
