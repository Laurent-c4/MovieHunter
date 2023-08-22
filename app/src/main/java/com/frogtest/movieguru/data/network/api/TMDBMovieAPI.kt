package com.frogtest.movieguru.data.network.api

import com.frogtest.movieguru.BuildConfig
import com.frogtest.movieguru.data.network.apiresponses.MoviesResponse
import com.frogtest.movieguru.data.network.dto.movie_details.MovieDetails2Dto
import com.frogtest.movieguru.data.network.dto.movie_details.MovieDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBMovieAPI {
    @GET("trending/{type}/{length}")
    suspend fun getTrendingMovies(
        @Path("type") type: String = "movie",
        @Path("length") length: String = "day",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = API_KEY,
    ): MoviesResponse

    @GET("{type}/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Path("type") type: String,
        @Query("language") language: String = "en-US",
        @Query("append_to_response") appendToResponse: String = "videos,credits",
        @Query("api_key") apiKey: String = API_KEY,
    ): MovieDetailsDto

    @GET("find/{external_id}")
    suspend fun getMovieDetailsByExternalId(
        @Path("external_id") externalID: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("external_source") externalSource: String = "imdb_id",

    ): MovieDetails2Dto


    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = BuildConfig.TMDB_API_KEY
    }
}