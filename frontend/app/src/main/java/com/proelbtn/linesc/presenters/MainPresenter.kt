package com.proelbtn.linesc.presenters

import android.util.Log
import com.proelbtn.linesc.models.containers.HomeAdapterDataContainer
import com.proelbtn.linesc.models.datainterface.UserRelations
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainPresenter (val view: View) {
    val homeData = HomeAdapterDataContainer()
    var flag = false

    fun onCreateHomeFragmentView() {
        if (!flag) {
            UserRelations.create().getRelations()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            {
                                Log.d("MainPresenter", "Data Fetched")

                                flag = true
                                it.from.forEach { homeData.addFromRelation(it) }
                                it.to.forEach { homeData.addToRelation(it) }

                                view.updateHomeFragment()
                            },
                            {
                                Log.d("test", it.toString())
                            }
                    )
        }

    }

    fun onCreateDashboardFragmentView() {

    }

    fun onStartHomeFragment() {
        view.attachDataToHomeFragment(homeData)
    }

    fun onItemClicked(uid: String) {
        view.navigateToChatActivity(uid)
    }

    fun onSelectHome() {
        view.showHome()
    }

    fun onSelectDashboard() {
        view.showDashboard()
    }

    interface View {
        fun attachDataToHomeFragment(data: HomeAdapterDataContainer)
        fun updateHomeFragment()

        fun showHome()
        fun showDashboard()

        fun navigateToChatActivity(uid: String)
    }
}