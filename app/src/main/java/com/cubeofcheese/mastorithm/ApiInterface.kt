package com.cubeofcheese.mastorithm

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("https://mstdn.social/api/v1/timelines/public?limit=1")
    fun getData(): Call<List<TestData>>
}