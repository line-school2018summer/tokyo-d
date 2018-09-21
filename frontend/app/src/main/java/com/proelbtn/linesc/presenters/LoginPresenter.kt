package com.proelbtn.linesc.presenters

import android.content.Context
import com.proelbtn.linesc.managers.DataManager

class LoginPresenter (val view: View) {
    fun onLogin() {
        val sid = view.getSid()
        val pass = view.getPassword()

        DataManager.setSid(sid)
        DataManager.setPass(pass)

        view.navigateToMainActivity()
    }

    interface View {
        fun getContext(): Context
        fun getSid(): String
        fun getPassword(): String

        fun navigateToMainActivity()
    }
}