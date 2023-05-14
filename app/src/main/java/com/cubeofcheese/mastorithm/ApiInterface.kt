package com.cubeofcheese.mastorithm

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @Headers("Authorization: Bearer <authcode>")
    @GET("https://mstdn.social/api/v1/timelines/home")
    fun getData(@Query("since_id") sinceId: String?): Call<List<TestData>>

    @Headers("Authorization: Bearer <authcode>")
    @GET("https://mstdn.social/api/v1/trends/statuses")
    fun getTrendingStatuses(): Call<List<TestData>>

    @Headers("Authorization: Bearer <authcode>")
    @GET("https://mstdn.social/api/v1/timelines/public?local=true")
    fun getLocalStatuses(@Query("since_id") sinceId: String?): Call<List<TestData>>
}