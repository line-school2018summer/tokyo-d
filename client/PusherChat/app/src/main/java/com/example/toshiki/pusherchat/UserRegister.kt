package com.example.toshiki.pusherchat

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRegister {
    @POST("users")
    fun postUserRegister(@Body body:User): Call<Identification>

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"
//        private const val BASE_URL = "http://requestbin.fullcontact.com/16hyms31/"

        fun create(): UserRegister {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(UserRegister::class.java)
        }
    }
}