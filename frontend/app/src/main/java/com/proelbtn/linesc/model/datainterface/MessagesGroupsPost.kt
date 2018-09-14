package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.model.dataclass.PostMessagesGroups
import com.proelbtn.linesc.model.dataclass.ResPostToken
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MessagesGroupsPost {
    @Multipart
    @POST("groups")
    fun postMessagesGroups(@Part("user") user: ResPostToken, @Part("req") req: PostMessagesGroups): Void

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"

        fun create(): MessagesGroupsPost {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(MessagesGroupsPost::class.java)
        }
    }
}