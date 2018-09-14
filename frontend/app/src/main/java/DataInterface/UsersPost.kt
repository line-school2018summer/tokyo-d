package DataInterface

import DataClass.PostUsers
import DataClass.ResPostUsers
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersPost {
    @POST("users")
    fun postUsers(@Body body:PostUsers): Call<ResPostUsers>

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"

        fun create(): UsersPost {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(UsersPost::class.java)
        }
    }
}