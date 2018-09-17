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
import com.proelbtn.linesc.adapters.HomeAdapter
import com.proelbtn.linesc.presenters.HomePresenter

class HomeFragment : Fragment(), HomePresenter.View {
    val presenter = HomePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rv_home)

        presenter.onCreateView()

        val data = ArrayList<String>()
        data.add("Friend Blue")
        data.add("Friend Yellow")
        data.add("Friend Red")

        val adapter = HomeAdapter(context!!, data)

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity)

        return view
    }
}
