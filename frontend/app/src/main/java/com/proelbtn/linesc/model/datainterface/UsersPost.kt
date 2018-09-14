package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.model.dataclass.PostUsers
import com.proelbtn.linesc.model.dataclass.ResPostUsers
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersPost {
    @POST("users")
    fun postUsers(@Body body: PostUsers): Call<ResPostUsers>

    companion object {
        fun create(): UsersPost {
            return retrofit.create(UsersPost::class.java)
        }
    }
}