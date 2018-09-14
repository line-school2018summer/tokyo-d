package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.model.dataclass.ResDeleteUsers
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Path

interface UsersDelete {
    @DELETE("users/{sid}")
    fun deleteUsers(@Path("sid") sid: String): Single<ResDeleteUsers>

    companion object {
        fun create(): UsersDelete {
            return retrofit.create(UsersDelete::class.java)
        }
    }
}