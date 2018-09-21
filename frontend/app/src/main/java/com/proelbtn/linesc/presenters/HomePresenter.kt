package com.proelbtn.linesc.presenters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.proelbtn.linesc.adapters.HomeAdapter
import com.proelbtn.linesc.models.datainterface.UserRelations
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class HomePresenter(val view: View) {
    val data = ArrayList<Pair<String, String>>()
    var adapter: HomeAdapter? = null
    var flag = false

    fun onCreate() {
        if (!flag) {
            UserRelations.create().getRelations()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            {
                                flag = true
                                it.from.forEach {
                                    data.add(Pair(it.id, it.name))
                                }
                                it.to.forEach {
                                    data.add(Pair(it.id, it.name))
                                }
                                adapter?.notifyDataSetChanged()
                            },
                            {
                                Log.d("test", it.toString())
                            }
                    )
        }
    }

    fun onStart() {
        val rv = view.getRecyclerView()!!
        adapter = HomeAdapter(view.getContext()!!, data)

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(view.getContext()!!)
    }

    interface View {
        fun getRecyclerView(): RecyclerView?
        fun getContext(): Context?
    }
}