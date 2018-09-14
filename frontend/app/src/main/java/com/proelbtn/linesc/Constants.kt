package com.proelbtn.linesc

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Constants {
    val baseUrl = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"
    val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
}
