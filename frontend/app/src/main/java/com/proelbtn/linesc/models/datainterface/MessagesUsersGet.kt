package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.retrofit
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET

interface MessagesUsersGet {
    @GET("messages/users")
    fun getMessegesUsers(@Body token: String): Single<Unit>

    companion object {
        fun create(): MessagesUsersGet {
            return retrofit.create(MessagesUsersGet::class.java)
        }
    }
}
