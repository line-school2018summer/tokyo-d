package com.proelbtn.linesc.presenters

class MainPresenter (val view: View) {
    fun selectHome() {
        view.showHome()
    }

    fun selectDashboard() {
        view.showDashboard()
    }

    fun selectNotifications() {
        view.showNotifications()
    }

    interface View {
        fun showHome()
        fun showDashboard()
        fun showNotifications()
    }
}