package com.frogtest.movieguru.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.frogtest.movieguru.MovieApp
import com.frogtest.movieguru.data.cache.MovieDatabase
import com.frogtest.movieguru.data.cache.entity.MovieEntity
import com.frogtest.movieguru.data.network.MovieNetworkMediator
import com.frogtest.movieguru.data.network.api.OMDBMovieAPI
import com.frogtest.movieguru.data.network.api.TMDBMovieAPI
import com.frogtest.movieguru.data.repository.AuthRepositoryImpl
import com.frogtest.movieguru.domain.repository.AuthRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MovieApp {
        return app as MovieApp
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(): OMDBMovieAPI {
        return Retrofit.Builder()
            .baseUrl(OMDBMovieAPI.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(OMDBMovieAPI::class.java)
    }

    @Provides
    @Singleton
        fun provideMovieApi2(): TMDBMovieAPI {
        return Retrofit.Builder()
            .baseUrl(TMDBMovieAPI.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TMDBMovieAPI::class.java)
    }

    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth
    )

}