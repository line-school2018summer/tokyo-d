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
import com.proelbtn.linesc.adapters.HomeAdapter
import com.proelbtn.linesc.models.containers.HomeAdapterDataContainer

class HomeFragment : Fragment(), HomeAdapter.Listener {
    var listener: Listener? = null
    var adapter: HomeAdapter? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) listener = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        listener?.onCreateFragmentView(this)

        val rv = view.findViewById<RecyclerView>(R.id.rv_home)

        adapter = HomeAdapter(context!!)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context!!)

        adapter?.listener = this

        return view
    }

    override fun onStart() {
        super.onStart()
        listener?.onStartFragment(this)
    }

    override fun onItemClicked(uid: String) {
        listener?.onItemClicked(uid)
    }

    fun attachData(data: HomeAdapterDataContainer) {
        adapter?.data = data
        update()
    }

    fun update() {
        adapter?.notifyDataSetChanged()
    }


    interface Listener {
        fun onCreateFragmentView(fragment: Fragment)
        fun onStartFragment(fragment: Fragment)
        fun onItemClicked(uid: String)
    }
}
