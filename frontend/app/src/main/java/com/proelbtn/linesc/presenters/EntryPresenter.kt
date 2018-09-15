package com.proelbtn.linesc.activities

import android.util.Log

class EntryPresenter (val view: View) {
    fun onCreate() {
    }

    fun onLogin() {
        view.navigateToLoginActivity()
    }

    fun onSignup() {
        view.navigateToSignupActivity()
    }

    interface View {
        fun navigateToLoginActivity()
        fun navigateToSignupActivity()
        fun navigateToMainActivity()
    }
}
