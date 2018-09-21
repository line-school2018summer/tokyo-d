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
import com.proelbtn.linesc.presenters.HomePresenter

class HomeFragment : Fragment(), HomePresenter.View {
    val presenter = HomePresenter(this)
    var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) listener = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun getRecyclerView(): RecyclerView? {
        return view?.findViewById(R.id.rv_home)
    }

    interface Listener {
        fun onSelectUser(id: String)

    }
}
