package com.proelbtn.linesc.presenters

class MainPresenter (val view: View) {
    fun selectHome() {
        view.showHome()
    }

    fun selectDashboard() {
        view.showDashboard()
    }

    interface View {
        fun showHome()
        fun showDashboard()

        fun navigateToChatActivity()
    }
}