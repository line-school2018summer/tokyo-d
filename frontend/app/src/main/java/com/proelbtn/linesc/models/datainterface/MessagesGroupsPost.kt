package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.models.dataclass.PostMessagesGroups
import com.proelbtn.linesc.models.dataclass.ResPostToken
import io.reactivex.Single
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MessagesGroupsPost {
    @Multipart
    @POST("groups")
    fun postMessagesGroups(@Part("user") user: ResPostToken, @Part("req") req: PostMessagesGroups): Single<Unit>

    companion object {
        fun create(): MessagesGroupsPost {
            return retrofit.create(MessagesGroupsPost::class.java)
        }
    }
}