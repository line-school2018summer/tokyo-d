package com.proelbtn.linesc.presenters

import android.content.Intent
import com.proelbtn.linesc.adapters.HomeAdapter

class MainPresenter (val view: View): HomeAdapter.Listener {
    fun onSelectUser(id: String) {
        view.navigateToChatActivity(id)
    }

    fun onSelectHome() {
        view.showHome()
    }

    fun onSelectDashboard() {
        view.showDashboard()
    }

    override fun onClicked(id: String) {
    }

    interface View {
        fun onSelectUser(id: String)
        fun showHome()
        fun showDashboard()

        fun navigateToChatActivity(id: String)
    }
}