package com.frogtest.movieguru.data.network.api

import com.frogtest.movieguru.BuildConfig
import com.frogtest.movieguru.data.network.apiresponses.OMDBMoviesResponse
import com.frogtest.movieguru.data.network.dto.MovieDetailsIMDBDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBMovieAPI {

    @GET(".")
    suspend fun getMovies(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("s") title: String = "love",
        @Query("page") page: Int = 1
    ): OMDBMoviesResponse

    @GET(".")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("i") imdbID: String
    ): MovieDetailsIMDBDto

    companion object {
        const val BASE_URL = "https://www.omdbapi.com/"
        const val API_KEY = BuildConfig.OMDB_API_KEY
    }
}