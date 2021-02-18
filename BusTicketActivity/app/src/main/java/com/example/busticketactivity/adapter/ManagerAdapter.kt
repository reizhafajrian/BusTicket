package com.example.busticketactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.admin.getName
import com.example.busticketactivity.dataclass.InfoTiket
import com.example.busticketactivity.firebase.FireBaseRepo
import kotlinx.android.synthetic.main.item_rekap_tujuan.view.*


class ManagerAdapter(private val list: MutableList<InfoTiket>) :
    RecyclerView.Adapter<ManagerAdapter.ViewHolder>() {
    private val tag = "ManagerActivity"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: InfoTiket) {
            FireBaseRepo().getUserNama(item.email).addOnCompleteListener {
                if (it.isSuccessful) {
                    val data =
                        it.result?.toObjects(getName::class.java)
                    itemView.tv_nama_penumpang.text = data!![0].nama
                }
            }
            with(itemView) {
                with(itemView) {
                    tv_id.text = item.nomorKursi
                    tv_email.text = item.email
                    tv_harga.text = item.harga
                    tv_type_bus.text = item.type
                }
            }

        }
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rekap_tujuan, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }