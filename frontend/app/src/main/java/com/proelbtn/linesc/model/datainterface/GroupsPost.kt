package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.model.dataclass.PostGroups
import com.proelbtn.linesc.model.dataclass.ResPostGroups
import com.proelbtn.linesc.model.dataclass.ResPostToken
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GroupsPost {
    @Multipart
    @POST("groups")
    fun postGroups(@Part("user") user: ResPostToken, @Part("req") req: PostGroups): Call<ResPostGroups>

    companion object {
        fun create(): GroupsPost {
            return retrofit.create(GroupsPost::class.java)
        }
    }
}