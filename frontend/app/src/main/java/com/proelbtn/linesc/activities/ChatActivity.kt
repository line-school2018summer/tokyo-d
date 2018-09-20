package com.proelbtn.linesc.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.proelbtn.linesc.R
import com.proelbtn.linesc.presenters.ChatPresenter

class ChatActivity : AppCompatActivity(), ChatPresenter.View {
    val presenter = ChatPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }
}
