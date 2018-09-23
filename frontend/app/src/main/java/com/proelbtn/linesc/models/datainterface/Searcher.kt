package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.models.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Searcher {
    @GET("search/users/{sid}")
    fun searchUser(@Path("sid") uid: String): Single<User>

    companion object {
        fun create(): Searcher{
            return retrofit.create(Searcher::class.java)
        }
    }
}
