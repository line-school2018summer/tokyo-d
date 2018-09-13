package com.proelbtn.linesc.activities

import android.util.Log

class LoginPresenter (val view: View) {
    fun login() {
        val username = view.getUserName()
        val password = view.getPassword()

        view.navigateToMainActivity()
    }

    interface View {
        fun getUserName(): String
        fun getPassword(): String

        fun navigateToMainActivity()
    }
}
