package com.app.justimage.api

import com.app.justimage.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PixabayApi {

    // suspend fun to handle threading with kotlin coroutines
    @GET("api/")
    suspend fun searchPhotos(
            @Query("key") key: String?,
            @Query("q") query: String?,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
    ): PixabayResponse

    // static as in java
    companion object {
        val ACCESS_KEY_PIXABAY = "20321905-42ccc03392c3fdd2664acaf43"
        const val BASE_URL = "https://pixabay.com/"
    }
}