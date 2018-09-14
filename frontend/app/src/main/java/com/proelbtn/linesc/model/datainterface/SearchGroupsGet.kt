package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.model.dataclass.ResGetSearchGroups
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchGroupsGet {
    @GET("search/users/{sid}")
    fun getSearchGroups(@Path("sid") sid: String): Single<ResGetSearchGroups>

    companion object {
        fun create(): SearchGroupsGet {
            return retrofit.create(SearchGroupsGet::class.java)
        }
    }
}