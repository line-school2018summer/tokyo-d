package com.proelbtn.linesc.presenters

class LoginPresenter (val view: View) {
    fun onLogin() {
    }

    interface View {
        fun getId(): String
        fun getPassword(): String

        fun navigateToMainActivity()
    }
}