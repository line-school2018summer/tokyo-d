package com.example.toshiki.pusherchat


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class NotificationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_notification, container, false)

    companion object {
        fun newInstance(): NotificationFragment = NotificationFragment()
    }
}

