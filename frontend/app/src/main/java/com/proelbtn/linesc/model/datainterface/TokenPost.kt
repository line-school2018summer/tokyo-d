package com.proelbtn.linesc.model.datainterface

import com.proelbtn.linesc.Constants.retrofit
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
        fun create(): TokenPost {
            return retrofit.create(TokenPost::class.java)
        }
    }
}