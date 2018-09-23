package com.proelbtn.linesc.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.proelbtn.linesc.R
import com.proelbtn.linesc.models.containers.ChatAdapterDataContainer

class ChatAdapter (context: Context): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    val inflater = LayoutInflater.from(context)
    var data: ChatAdapterDataContainer? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cell_chat, p0, false))
    }

    override fun getItemCount(): Int {
        return data?.getItemCount() ?: 0
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val message = data?.get(p1)
        p0.nameView.text = message?.from
        p0.timestampView.text = message?.created_at
        val content = String(Base64.decode(message?.content, Base64.DEFAULT))
        p0.contentView.text = content
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.text_name)
        val timestampView = itemView.findViewById<TextView>(R.id.text_timestamp)
        val contentView = itemView.findViewById<TextView>(R.id.text_content)
    }
}