package com.proelbtn.linesc.presenters

import android.content.Context
import android.widget.Toast
import com.proelbtn.linesc.managers.StoredDataManager
import com.proelbtn.linesc.models.datainterface.SearchUsersGet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenter (val view: View) {
    fun onLogin() {
        val sid = view.getSid()
        val pass = view.getPassword()

        if (sid.isEmpty() or pass.isEmpty()) {
            Toast.makeText(view.getContext(),"Username and Password should not be empty", Toast.LENGTH_SHORT).show()
        } else {
            StoredDataManager.setSid(sid)
            StoredDataManager.setPass(pass)
            view.navigateToEntryActivity()
        }
    }

    interface View {
        fun getContext(): Context
        fun getSid(): String
        fun getPassword(): String

        fun navigateToEntryActivity()
    }
}
