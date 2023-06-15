package com.cubeofcheese.mastorithm

import com.cubeofcheese.mastorithm.models.Application
import com.cubeofcheese.mastorithm.models.StatusContext
import com.cubeofcheese.mastorithm.models.TestData
import com.cubeofcheese.mastorithm.models.Token
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @FormUrlEncoded
    @POST("api/v1/apps")
    fun authenticateApp(@Field("client_name") clientName: String,
                        @Field("redirect_uris") redirectUris: String,
                        @Field("scopes") scopes: String,
                        @Field("website") website: String): Call<Application>
    @FormUrlEncoded
    @POST("/oauth/token")
    fun getOauthToken(@Field("client_id") clientId: String,
                        @Field("client_secret") clientSecret: String,
                        @Field("redirect_uri") redirectUri: String,
                        @Field("grant_type") grantType: String): Call<Token>

    @FormUrlEncoded
    @POST("/oauth/token")
    fun getOauthToken(@Field("client_id") clientId: String,
                        @Field("client_secret") clientSecret: String,
                        @Field("redirect_uri") redirectUri: String,
                        @Field("grant_type") grantType: String,
                        @Field("code") code: String,
                        @Field("scope") scope: String): Call<Token>

    @GET("api/v1/timelines/home")
    fun getData(@Header("Authorization") bearerToken: String,
                @Query("since_id") sinceId: String?,
                @Query("max_id") maxId: String?): Call<List<TestData>>

    @GET("api/v1/trends/statuses")
    fun getTrendingStatuses(@Header("Authorization") bearerToken: String,
                            @Query("offset") offset: Int?): Call<List<TestData>>

    @GET("api/v1/timelines/public?local=true")
    fun getLocalStatuses(@Header("Authorization") bearerToken: String,
                         @Query("since_id") sinceId: String?,
                         @Query("max_id") maxId: String?): Call<List<TestData>>

    @GET("api/v1/statuses/{statusId}/context")
    fun getStatusContext(@Header("Authorization") bearerToken: String,
                         @Path("statusId") statusId: String): Call<StatusContext>

    @POST("api/v1/statuses/{statusId}/reblog")
    fun boostStatus(@Header("Authorization") bearerToken: String,
                    @Path("statusId") statusId: String): Call<TestData>

    @POST("api/v1/statuses/{statusId}/favourite")
    fun favouriteStatus(@Header("Authorization") bearerToken: String,
                        @Path("statusId") statusId: String): Call<TestData>

}