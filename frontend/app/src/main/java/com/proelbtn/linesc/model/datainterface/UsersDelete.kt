package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.model.dataclass.ResDeleteUsers
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Path

interface UsersDelete {
    @DELETE("users/{sid}")
    fun deleteUsers(@Path("sid") sid: String): Call<ResDeleteUsers>

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"

        fun create(): UsersDelete {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(UsersDelete::class.java)
        }
    }
}