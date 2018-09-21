package com.proelbtn.linesc.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.proelbtn.linesc.R
import android.widget.Toast
import android.support.design.widget.Snackbar

class HomeAdapter(context: Context, data: ArrayList<Pair<String, String>>, listener: Listener): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    val inflater = LayoutInflater.from(context)
    val data = data
    val listener = listener

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cell_home, p0, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.idView.text = data[p1].first
        p0.nameView.text = data[p1].second
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val idView: TextView = itemView.findViewById(R.id.text_id)
        val nameView: TextView = itemView.findViewById(R.id.text_name)

        init {
            itemView.setOnClickListener {
                listener.onClicked(data[adapterPosition].first)
                /*
                val position = adapterPosition
                Snackbar.make(it, "Click detected on item $position",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show()
                        */
            }
        }
    }

    interface Listener {
        fun onClicked(id: String)
    }
}