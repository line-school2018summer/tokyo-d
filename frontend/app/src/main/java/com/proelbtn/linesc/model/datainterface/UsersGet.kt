package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.model.dataclass.ResGetUsers
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Path
import retrofit2.http.GET

interface UsersGet {
    @GET("users/{sid}")
    fun getUsers(@Path("sid") sid: String): Single<ResGetUsers>

    companion object {
        fun create(): UsersGet {
            return retrofit.create(UsersGet::class.java)
        }
    }
}