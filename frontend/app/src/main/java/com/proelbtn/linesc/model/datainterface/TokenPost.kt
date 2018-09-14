package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.model.dataclass.PostToken
import com.proelbtn.linesc.model.dataclass.ResPostToken
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface TokenPost {
    @POST("token")
    fun postToken(@Body body: PostToken): Call<ResPostToken>

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"

        fun create(): TokenPost {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(TokenPost::class.java)
        }
    }
}