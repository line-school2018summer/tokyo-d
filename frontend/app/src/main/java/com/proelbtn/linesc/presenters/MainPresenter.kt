package com.proelbtn.linesc.presenters

import android.content.Intent

class MainPresenter (val view: View) {
    fun onSelectUser(id: String) {
        view.navigateToChatActivity(id)
    }

    fun onSelectHome() {
        view.showHome()
    }

    fun onSelectDashboard() {
        view.showDashboard()
    }

    interface View {
        fun onSelectUser(id: String)
        fun showHome()
        fun showDashboard()

        fun navigateToChatActivity(id: String)
    }
}