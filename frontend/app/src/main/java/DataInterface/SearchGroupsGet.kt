package DataInterface

import DataClass.ResGetSearchGroups
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchGroupsGet {
    @GET("search/users/{sid}")
    fun getSearchGroups(@Path("sid") sid: String): Call<ResGetSearchGroups>

    companion object {
        private const val BASE_URL = "http://ec2-52-194-219-150.ap-northeast-1.compute.amazonaws.com/api/"

        fun create(): SearchGroupsGet {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(SearchGroupsGet::class.java)
        }
    }
}