package com.proelbtn.linesc.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.proelbtn.linesc.R
import com.proelbtn.linesc.adapters.ChatAdapter
import com.proelbtn.linesc.models.containers.ChatAdapterDataContainer
import com.proelbtn.linesc.presenters.ChatPresenter

class ChatActivity : AppCompatActivity(), ChatPresenter.View {
    val presenter = ChatPresenter(this)
    var adapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_chat)

        adapter = ChatAdapter(this)

        presenter.onCreate(intent.getStringExtra("uid")!!)

        findViewById<Button>(R.id.button_send).setOnClickListener {
            val content = findViewById<TextView>(R.id.text_content).text

            presenter.sendMessage(content.toString())
        }

        val rv = findViewById<RecyclerView>(R.id.rv_chat)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun attachData(data: ChatAdapterDataContainer) {
        adapter?.data = data
        adapter?.notifyDataSetChanged()
    }

    override fun notifyItemInserted(pos: Int) {
        adapter?.notifyItemInserted(pos)
    }

    override fun clearTextView() {
        findViewById<TextView>(R.id.text_content).text = ""
    }
}
