package com.proelbtn.linesc.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.proelbtn.linesc.R

class DashboardAdapter(context: Context, data: ArrayList<String>): RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {
    val inflater = LayoutInflater.from(context)
    val data = data

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cell_dashboard, p0, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.nameView.text = data[p1]
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameView: TextView

        init {
            nameView = itemView.findViewById(R.id.text_name)
        }
    }
}