package com.example.busticketactivity.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.InfoTiket
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.listener.ListenerPickTicket
import kotlinx.android.synthetic.main.activity_detail_rekap2.*
import kotlinx.android.synthetic.main.item_rekap_tujuan.view.*

class RekapAdapter(private val list: MutableList<InfoTiket?>, var listener: ListenerPickTicket):RecyclerView.Adapter<RekapAdapter.ListViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_rekap_tujuan,parent,false)
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
        fun bind(item:InfoTiket){
            FireBaseRepo().getUserNama(item.email).addOnCompleteListener {
                if (it.isSuccessful) {
                    val data =
                        it.result?.toObjects(getName::class.java)
                    itemView.tv_nama_penumpang.text = data!![0].nama
                }
            }
            with(itemView){
                tv_id.text=item.nomorKursi
                tv_email.text=item.email
                tv_harga.text=item.harga
                tv_type_bus.text=item.type

                cv_item.setOnClickListener { listener.onClicks(item.nomorKursi) }


            }
        }
    }
}

