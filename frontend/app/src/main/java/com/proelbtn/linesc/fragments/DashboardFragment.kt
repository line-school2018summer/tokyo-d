package com.proelbtn.linesc.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.proelbtn.linesc.R
import com.proelbtn.linesc.adapters.DashboardAdapter

class DashboardFragment : Fragment() {
    var listener: Listener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) listener = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        listener?.onCreateFragmentView(this)

        val rv = view.findViewById<RecyclerView>(R.id.rv_dashboard)

        val data = ArrayList<String>()
        data.add("Hello, World. 1")
        data.add("Hello, World. 2")
        data.add("Hello, World. 3")

        val adapter = DashboardAdapter(context!!, data)

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity)

        return view
    }

    interface Listener {
        fun onCreateFragmentView(fragment: Fragment)
    }
}
