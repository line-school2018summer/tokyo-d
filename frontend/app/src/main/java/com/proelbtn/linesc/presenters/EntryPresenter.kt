package com.proelbtn.linesc.activities

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast.LENGTH_SHORT
import com.proelbtn.linesc.managers.DataManager
import com.proelbtn.linesc.models.dataclass.PostToken
import com.proelbtn.linesc.models.datainterface.TokenPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EntryPresenter (val view: View) {
    fun onCreate() {
        val sid = DataManager.getSid()
        val pass = DataManager.getPass()

        Log.d("test3", sid.toString())
        Log.d("test3", pass.toString())

        if (sid != null && pass != null) {
            TokenPost.create()
                    .postToken(PostToken(sid, pass))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            {
                                DataManager.setToken(it.token)
                                view.navigateToMainActivity()
                            },
                            {
                                view.showMessage("Couldn't Log in...", LENGTH_SHORT)
                                DataManager.removeSid()
                                DataManager.removePass()
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
        fun navigateToMainActivity()
    }
}
