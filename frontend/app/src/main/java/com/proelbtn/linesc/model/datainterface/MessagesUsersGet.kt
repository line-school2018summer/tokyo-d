package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET

interface MessagesUsersGet {
    @GET("messages/users")
    fun getMessegesUsers(@Body token: String): Void

    companion object {
        fun create(): MessagesUsersGet {
            return retrofit.create(MessagesUsersGet::class.java)
        }
    }
}
