package DataInterface

import DataClass.ResGetUsers
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Path
import retrofit2.http.GET

interface UsersGet {
    @GET("users/{sid}")
    fun getUsers(@Path("sid") sid: String): Call<ResGetUsers>

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"

        fun create(): UsersGet {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(UsersGet::class.java)
        }
    }
}