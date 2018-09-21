package com.proelbtn.linesc.managers

import android.content.Context.MODE_PRIVATE
import android.preference.PreferenceManager
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class DataManager: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("test2", getToken())
        val req = chain.request()

        return chain.proceed(req.newBuilder()
                .addHeader("Authorization", "Bearer " + getToken())
                .build())
    }

    companion object {
        fun getId(): String? {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            return pref.getString("id", null)
        }

        fun getSid(): String? {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            return pref.getString("sid", null)

        }

        fun getPass(): String? {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            return pref.getString("pass", null)

        }

        fun getToken(): String? {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            return pref.getString("token", null)
        }

        fun setId(id: String) {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            pref.edit().putString("id", id).apply()
        }

        fun setSid(sid: String) {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            pref.edit().putString("sid", sid).apply()
        }

        fun setPass(pass: String) {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            pref.edit().putString("pass", pass).apply()
        }

        fun setToken(token: String) {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            pref.edit().putString("token", token).apply()
        }

        fun removeId() {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            pref.edit().remove("id").apply()
        }

        fun removeSid() {
            val pref = ContextManager.getContext()!!.getSharedPreferences("sp", MODE_PRIVATE)
            pref.edit().remove("sid").apply()
        }

        fun removePass() {
            val pref = PreferenceManager.getDefaultSharedPreferences(ContextManager.getContext()!!)
            pref.edit().remove("pass").apply()
        }

        fun removeToken() {
            val pref = PreferenceManager.getDefaultSharedPreferences(ContextManager.getContext()!!)
            pref.edit().remove("token").apply()
        }
    }
}