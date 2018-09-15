package com.proelbtn.linesc.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.proelbtn.linesc.R
import com.proelbtn.linesc.presenters.NotificationsPresenter

class NotificationsFragment : Fragment(), NotificationsPresenter.View {
    val presenter = NotificationsPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }
}
