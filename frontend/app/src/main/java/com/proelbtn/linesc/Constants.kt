package com.proelbtn.linesc

import com.proelbtn.linesc.managers.StoredDataManager
import com.proelbtn.linesc.managers.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object Constants {
    val baseUrl = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"
    val authedClient = OkHttpClient().newBuilder()
            .addInterceptor(TokenManager())
            .build()

    val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    val authedRetrofit = Retrofit.Builder()
            .client(authedClient)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
}
