package com.cubeofcheese.mastorithm

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiInterface {
    @Headers("Authorization: Bearer <authcode>")
    @GET("https://mstdn.social/api/v1/timelines/home?limit=10")
    fun getData(): Call<List<TestData>>
}