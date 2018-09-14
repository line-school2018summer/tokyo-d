package com.proelbtn.linesc.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.proelbtn.linesc.R
import com.proelbtn.linesc.presenters.DashboardPresenter

class DashboardFragment : Fragment(), DashboardPresenter.View {
    val view = DashboardPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dashboard, container, false)
    }
}