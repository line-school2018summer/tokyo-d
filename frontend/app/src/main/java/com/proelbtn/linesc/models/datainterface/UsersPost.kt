package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.models.dataclass.PostUsers
import com.proelbtn.linesc.models.dataclass.ResPostUsers
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersPost {
    @POST("users")
    fun postUsers(@Body body: PostUsers): Single<ResPostUsers>

    companion object {
        fun create(): UsersPost {
            return retrofit.create(UsersPost::class.java)
        }
    }
}