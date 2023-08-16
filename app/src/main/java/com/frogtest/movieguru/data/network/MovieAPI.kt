package com.frogtest.movieguru.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    @GET(".")
    suspend fun getMovies(
        @Query("apikey") apiKey: String = "ff196f56",
        @Query("s") title: String = "love",
        @Query("page") page: Int = 1
    ): MovieResponse

    companion object {
        const val BASE_URL = "https://www.omdbapi.com/"
    }
}