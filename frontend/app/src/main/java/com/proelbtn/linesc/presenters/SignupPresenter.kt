package com.proelbtn.linesc.presenters

import com.proelbtn.linesc.model.dataclass.PostUsers
import com.proelbtn.linesc.model.datainterface.UsersPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignupPresenter (val view: View) {
    fun onSignup() {
        val id = view.getId()
        val name = view.getName()
        val pass = view.getPassword()

        /*
        UsersPost.create()
                .postUsers(PostUsers(id, name, pass))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                }
                */

        view.navigateToMainActivity()
    }

    interface View {
        fun getId(): String
        fun getName(): String
        fun getPassword(): String

        fun navigateToMainActivity()
    }
}