package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.models.dataclass.PostMessagesUsers
import com.proelbtn.linesc.models.dataclass.ResPostToken
import io.reactivex.Single
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface MessagesUsersPost {
    @Multipart
    @POST("messages/users")
    fun postMessageUsers(@Part("user") user: ResPostToken, @Part("req") req: PostMessagesUsers): Single<Unit>

    companion object {
        fun create(): MessagesUsersPost {
            return retrofit.create(MessagesUsersPost::class.java)
        }
    }
}