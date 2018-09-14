package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.model.dataclass.PostMessagesUsers
import com.proelbtn.linesc.model.dataclass.ResPostToken
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface MessagesUsersPost {
    @Multipart
    @POST("messages/users")
    fun postMessageUsers(@Part("user") user: ResPostToken, @Part("req") req: PostMessagesUsers): Void

    companion object {
        fun create(): MessagesUsersPost {
            return retrofit.create(MessagesUsersPost::class.java)
        }
    }
}