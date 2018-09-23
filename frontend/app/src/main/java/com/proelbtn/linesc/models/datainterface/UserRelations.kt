package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.authedRetrofit
import com.proelbtn.linesc.models.responses.RelationResponse
import io.reactivex.Single
import retrofit2.http.GET

interface UserRelations {
    @GET("relations/users")
    fun getRelations(): Single<RelationResponse>

    companion object {
        fun create(): UserRelations {
            return authedRetrofit.create(UserRelations::class.java)
        }
    }
}