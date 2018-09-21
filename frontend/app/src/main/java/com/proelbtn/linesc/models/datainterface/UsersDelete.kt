package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.retrofit
import com.proelbtn.linesc.models.dataclass.ResDeleteUsers
import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.Path

interface UsersDelete {
    @DELETE("users/{sid}")
    fun deleteUsers(@Path("sid") sid: String): Single<ResDeleteUsers>

    companion object {
        fun create(): UsersDelete {
            return retrofit.create(UsersDelete::class.java)
        }
    }
}