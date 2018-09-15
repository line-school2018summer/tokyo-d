package com.proelbtn.linesc.presenters

class SignupPresenter (val view: View) {
    fun onSignup() {
    }

    interface View {
        fun getId(): String
        fun getName(): String
        fun getPassword(): String

        fun navigateToMainActivity()
    }
}