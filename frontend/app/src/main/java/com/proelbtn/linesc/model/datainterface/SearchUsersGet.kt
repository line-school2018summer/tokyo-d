package com.proelbtn.linesc.model.datainterface

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
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"

        fun create(): SearchUsersGet {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(SearchUsersGet::class.java)
        }
    }
}
