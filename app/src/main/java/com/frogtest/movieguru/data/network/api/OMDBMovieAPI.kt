package com.frogtest.movieguru.data.network.api

import com.frogtest.movieguru.data.network.apiresponses.MoviesResponse
import com.frogtest.movieguru.data.network.dto.MovieDetailsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBMovieAPI {

    @GET(".")
    suspend fun getMovies(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("s") title: String = "love",
        @Query("page") page: Int = 1
    ): MoviesResponse

    @GET(".")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("i") imdbID: String
    ): MovieDetailsDto

    companion object {
        const val BASE_URL = "https://www.omdbapi.com/"
        const val API_KEY = "ff196f56"
    }
}