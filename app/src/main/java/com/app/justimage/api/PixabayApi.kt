package com.app.justimage.api

import com.app.justimage.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PixabayApi {

    // suspend fun to handle threading with kotlin coroutines
    @GET("search/users")
    suspend fun searchPhotos(
            @Query("q") query: String,
            @Query("sort") sort: String,
            @Query("page") page: Int = 1,
            @Query("per_page") numOfUsers: Int = 9,
    ): SearchUsersResponse

    // static as in java
    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}