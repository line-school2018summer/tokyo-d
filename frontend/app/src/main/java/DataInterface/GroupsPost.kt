package DataInterface

import DataClass.PostGroups
import DataClass.ResPostGroups
import DataClass.ResPostToken
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GroupsPost {
    @Multipart
    @POST("groups")
    fun postGroups(@Part("user") user:ResPostToken, @Part("req") req: PostGroups): Call<ResPostGroups>

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"

        fun create(): GroupsPost {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(GroupsPost::class.java)
        }
    }
}