package com.proelbtn.linesc.managers

import android.app.Application
import android.content.Context

class ContextManager: Application() {
    override fun onCreate() {
        super.onCreate()

        app = this
    }

    companion object {
        var app: Application? = null

        fun getContext(): Context? {
            return app?.applicationContext
        }
    }
}