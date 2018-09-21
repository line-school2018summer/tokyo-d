package com.proelbtn.linesc.presenters

import android.content.Context
import android.content.Context.MODE_PRIVATE

class LoginPresenter (val view: View) {
    fun onLogin() {
        val id = view.getId()
        val pass = view.getPassword()

        val pref = view.getContext().getSharedPreferences("keystore", MODE_PRIVATE)

        pref.edit()
                .putString("sid", view.getId())
                .putString("pass", view.getPassword())
                .apply()

        view.navigateToMainActivity()
    }

    interface View {
        fun getContext(): Context
        fun getId(): String
        fun getPassword(): String

        fun navigateToMainActivity()
    }
}