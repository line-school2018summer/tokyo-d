package com.proelbtn.linesc.activities

import android.content.Context
import android.util.Log
import android.widget.Toast.LENGTH_SHORT
import com.proelbtn.linesc.managers.StoredDataManager
import com.proelbtn.linesc.models.dataclass.PostToken
import com.proelbtn.linesc.models.datainterface.Searcher
import com.proelbtn.linesc.models.datainterface.TokenPost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EntryPresenter (val view: View) {
    fun onCreate() {
        val sid = StoredDataManager.getSid()
        val pass = StoredDataManager.getPass()

        Log.d("test3", sid.toString())
        Log.d("test3", pass.toString())

        if (sid != null && pass != null) {
            Searcher.create()
                    .searchUser(sid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            {
                                StoredDataManager.setId(it.id)
                                StoredDataManager.setSid(it.sid)
                                TokenPost.create()
                                        .postToken(PostToken(sid, pass))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe (
                                                {
                                                    StoredDataManager.setToken(it.token)
                                                    view.navigateToMainActivity()
                                                },
                                                {
                                                    view.showMessage("Couldn't Log in...", LENGTH_SHORT)
                                                    StoredDataManager.removeSid()
                                                    StoredDataManager.removePass()
                                                }
                                        )
                            },
                            {
                                view.showMessage("No User...", LENGTH_SHORT)
                                StoredDataManager.removeSid()
                                StoredDataManager.removePass()
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
