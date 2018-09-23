package com.proelbtn.linesc.models.datainterface

import com.proelbtn.linesc.Constants.authedRetrofit
import com.proelbtn.linesc.models.Message
import com.proelbtn.linesc.models.requests.MessageRequest
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface UserMessages {
    @GET("messages/users/{id}")
    fun getLatestMessages(@Path("id") id: String, @Query("count") count: Int = 30): Observable<List<Message>>

    @GET("messages/users/{id}")
    fun getBeforeMessages(@Path("id") id: String, @Query("max_id") mid: String, @Query("count") count: Int = 30): Observable<List<Message>>

    @GET("messages/users/{id}")
    fun getAfterMessages(@Path("id") id: String, @Query("since_id") mid: String, @Query("count") count: Int = 30): Observable<List<Message>>

    @POST("messages/users")
    fun sendMessage(@Body content: MessageRequest): Single<String>

    companion object {
        fun create(): UserMessages {
            return authedRetrofit.create(UserMessages::class.java)
        }
    }
}