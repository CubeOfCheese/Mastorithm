package com.cubeofcheese.mastorithm

import com.cubeofcheese.mastorithm.models.StatusContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @Headers("Authorization: Bearer <authcode>")
    @GET("https://mstdn.social/api/v1/timelines/home")
    fun getData(@Query("since_id") sinceId: String?, @Query("max_id") maxId: String?): Call<List<TestData>>

    @Headers("Authorization: Bearer <authcode>")
    @GET("https://mstdn.social/api/v1/trends/statuses")
    fun getTrendingStatuses(@Query("offset") offset: Int?): Call<List<TestData>>

    @Headers("Authorization: Bearer <authcode>")
    @GET("https://mstdn.social/api/v1/timelines/public?local=true")
    fun getLocalStatuses(@Query("since_id") sinceId: String?, @Query("max_id") maxId: String?): Call<List<TestData>>

    @Headers("Authorization: Bearer <authcode>")
    @GET("https://mstdn.social/api/v1/statuses/{statusId}/context")
    fun getStatusContext(@Path("statusId") statusId: String): Call<StatusContext>

    @Headers("Authorization: Bearer <authcode>")
    @POST("https://mstdn.social/api/v1/statuses/{statusId}/reblog")
    fun boostStatus(@Path("statusId") statusId: String): Call<TestData>

    @Headers("Authorization: Bearer <authcode>")
    @POST("https://mstdn.social/api/v1/statuses/{statusId}/favourite")
    fun favouriteStatus(@Path("statusId") statusId: String): Call<TestData>

}