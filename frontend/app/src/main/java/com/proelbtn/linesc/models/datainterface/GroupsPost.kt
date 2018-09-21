package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.models.dataclass.PostGroups
import com.proelbtn.linesc.models.dataclass.ResPostGroups
import com.proelbtn.linesc.models.dataclass.ResPostToken
import io.reactivex.Single
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GroupsPost {
    @Multipart
    @POST("groups")
    fun postGroups(@Part("user") user: ResPostToken, @Part("req") req: PostGroups): Single<ResPostGroups>

    companion object {
        fun create(): GroupsPost {
            return retrofit.create(GroupsPost::class.java)
        }
    }
}