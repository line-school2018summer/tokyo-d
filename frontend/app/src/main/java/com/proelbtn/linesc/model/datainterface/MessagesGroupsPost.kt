package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.model.dataclass.PostMessagesGroups
import com.proelbtn.linesc.model.dataclass.ResPostToken
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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