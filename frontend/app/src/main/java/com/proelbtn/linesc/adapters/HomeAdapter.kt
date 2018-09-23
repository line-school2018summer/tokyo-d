package com.proelbtn.linesc.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.proelbtn.linesc.R
import com.proelbtn.linesc.models.containers.HomeAdapterDataContainer

class HomeAdapter(context: Context): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    val inflater = LayoutInflater.from(context)
    var listener: Listener? = null

    var data: HomeAdapterDataContainer? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cell_home, p0, false))
    }

    override fun getItemCount(): Int {
        return data?.getItemCount() ?: 0
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val uid = data?.getKeys()?.sorted()?.get(p1)

        if (uid != null) {
            p0.idView.text = data?.getUserFromId(uid)?.id
            p0.nameView.text = data?.getUserFromId(uid)?.name
        }
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val idView: TextView = itemView.findViewById(R.id.text_id)
        val nameView: TextView = itemView.findViewById(R.id.text_name)

        init {
            itemView.setOnClickListener {
                val uid = data?.getKeys()?.sorted()?.get(adapterPosition)

                listener?.onItemClicked(uid!!)
            }
        }
    }

    interface Listener {
        fun onItemClicked(uid: String)
    }
}