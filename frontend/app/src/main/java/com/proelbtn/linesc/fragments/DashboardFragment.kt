package com.proelbtn.linesc.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.proelbtn.linesc.R
import com.proelbtn.linesc.adapters.DashboardAdapter
import com.proelbtn.linesc.presenters.DashboardPresenter

class DashboardFragment : Fragment(), DashboardPresenter.View {
    val presenter = DashboardPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rv_dashboard)

        presenter.onCreateView()

        val data = ArrayList<String>()
        data.add("Hello, World. 1")
        data.add("Hello, World. 2")
        data.add("Hello, World. 3")

        val adapter = DashboardAdapter(context!!, data)

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity)

        return view
    }
}
