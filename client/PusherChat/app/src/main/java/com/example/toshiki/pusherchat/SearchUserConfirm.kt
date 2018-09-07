package com.example.toshiki.pusherchat

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Path
import retrofit2.http.GET

interface SearchUserConfirm {
    @GET("search/users/{sid}")
    fun postSearchUserConfirm(@Path("sid") sid: String): Call<Identification>

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"
//        private const val BASE_URL = "http://requestbin.fullcontact.com/16hyms31/"

        fun create(): SearchUserConfirm {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(SearchUserConfirm::class.java)
        }
    }
}
