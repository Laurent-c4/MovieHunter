package com.frogtest.movieguru.data.network.api

import com.frogtest.movieguru.BuildConfig
import com.frogtest.movieguru.data.network.apiresponses.MovieVideosResponse
import com.frogtest.movieguru.data.network.dto.MovieDetailsTMDBDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBMovieAPI {
    @GET("find/{external_id}")
    suspend fun getMovieDetails(
        @Path("external_id") externalID: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("external_source") externalSource: String = "imdb_id",

    ): MovieDetailsTMDBDto

    @GET("movie/{tmdb_id}/{route}")
    suspend fun getMovieVideos(
        @Path("tmdb_id") tmdbID: Int,
        @Path("route") route: String = "videos",
        @Query("api_key") apiKey: String = API_KEY,

    ): MovieVideosResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = BuildConfig.TMDB_API_KEY
    }
}