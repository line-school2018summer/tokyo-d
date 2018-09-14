package DataInterface

import DataClass.PostRelationsGroups
import DataClass.ResPostRelationsGroups
import DataClass.ResPostToken
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RelationsGroupsPost {
    @Multipart
    @POST("groups")
    fun postMessagesGroups(@Part("user") user: ResPostToken, @Part("req") req: PostRelationsGroups): Call<ResPostRelationsGroups>

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"

        fun create(): RelationsGroupsPost {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(RelationsGroupsPost::class.java)
        }
    }
}