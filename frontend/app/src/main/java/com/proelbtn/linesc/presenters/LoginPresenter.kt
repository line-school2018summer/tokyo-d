package com.proelbtn.linesc.presenters

import com.proelbtn.linesc.model.dataclass.PostToken
import com.proelbtn.linesc.model.datainterface.TokenPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenter (val view: View) {
    fun onLogin() {
        val id = view.getId()
        val pass = view.getPassword()

        /*
        TokenPost.create()
                .postToken(PostToken(id, pass))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                }
                */

        view.navigateToMainActivity()
    }

    interface View {
        fun getId(): String
        fun getPassword(): String

        fun navigateToMainActivity()
    }
}