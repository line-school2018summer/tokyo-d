package com.proelbtn.linesc.managers

import com.proelbtn.linesc.managers.StoredDataManager.Companion.getToken
import okhttp3.Interceptor
import okhttp3.Response

class TokenManager: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()

        return chain.proceed(req.newBuilder()
                .addHeader("Authorization", "Bearer " + getToken())
                .build())
    }
}