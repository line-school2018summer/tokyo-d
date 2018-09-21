package com.proelbtn.linesc.presenters

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.proelbtn.linesc.model.dataclass.PostUsers
import com.proelbtn.linesc.model.datainterface.UsersPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignupPresenter (val view: View) {
    fun onSignup() {
        val id = view.getId()
        val name = view.getName()
        val pass = view.getPassword()

        UsersPost.create()
                .postUsers(PostUsers(id, name, pass))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        {
                            val pref = view.getContext().getSharedPreferences("keystore", MODE_PRIVATE)

                            pref.edit()
                                    .putString("sid", it.sid)
                                    .putString("pass", view.getPassword())
                                    .apply()


                            view.navigateToEntryActivity()
                        },
                        {

                        }
                )
    }

    interface View {
        fun getContext(): Context

        fun getId(): String
        fun getName(): String
        fun getPassword(): String

        fun navigateToEntryActivity()
    }
}