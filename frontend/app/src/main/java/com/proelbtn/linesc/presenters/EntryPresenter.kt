package com.proelbtn.linesc.activities

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.widget.Toast.LENGTH_SHORT
import com.proelbtn.linesc.model.dataclass.PostToken
import com.proelbtn.linesc.model.datainterface.TokenPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class EntryPresenter (val view: View) {
    fun onCreate() {
        val pref = view.getContext().getSharedPreferences("keystore", MODE_PRIVATE)

        val sid = pref.getString("sid", null)
        val pass = pref.getString("pass", null)

        if (sid != null && pass != null) {
            TokenPost.create()
                    .postToken(PostToken(sid, pass))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            {
                                view.navigateToMainActivity(it.token)
                            },
                            {
                                view.showMessage("Couldn't Log in...", LENGTH_SHORT)
                                pref.edit().remove("sid").remove("pass").apply()
                            }
                    )
        }
    }

    fun onLogin() {
        view.navigateToLoginActivity()
    }

    fun onSignup() {
        view.navigateToSignupActivity()
    }

    interface View {
        fun getContext(): Context

        fun showMessage(msg: CharSequence, mode: Int)

        fun navigateToLoginActivity()
        fun navigateToSignupActivity()
        fun navigateToMainActivity(token: String)
    }
}
