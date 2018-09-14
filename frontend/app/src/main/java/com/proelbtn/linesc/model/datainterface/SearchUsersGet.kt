package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.model.dataclass.ResGetSearchUsers
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Path
import retrofit2.http.GET

interface SearchUsersGet {
    @GET("search/users/{sid}")
    fun getSearchUsers(@Path("sid") sid: String): Call<ResGetSearchUsers>

    companion object {
        fun create(): SearchUsersGet {
            return retrofit.create(SearchUsersGet::class.java)
        }
    }
}
