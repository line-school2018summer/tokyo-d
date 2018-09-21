package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.models.dataclass.PostRelationsGroups
import com.proelbtn.linesc.models.dataclass.ResPostRelationsGroups
import com.proelbtn.linesc.models.dataclass.ResPostToken
import io.reactivex.Single
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RelationsGroupsPost {
    @Multipart
    @POST("groups")
    fun postMessagesGroups(@Part("user") user: ResPostToken, @Part("req") req: PostRelationsGroups): Single<ResPostRelationsGroups>

    companion object {
        fun create(): RelationsGroupsPost {
            return retrofit.create(RelationsGroupsPost::class.java)
        }
    }
}