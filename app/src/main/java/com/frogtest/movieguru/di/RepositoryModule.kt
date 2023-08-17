package com.frogtest.movieguru.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.frogtest.movieguru.MovieApp
import com.frogtest.movieguru.data.cache.MovieDatabase
import com.frogtest.movieguru.data.cache.entity.MovieEntity
import com.frogtest.movieguru.data.network.api.OMDBMovieAPI
import com.frogtest.movieguru.data.network.MovieNetworkMediator
import com.frogtest.movieguru.data.network.api.TMDBMovieAPI
import com.frogtest.movieguru.data.repository.MovieRepositoryImpl
import com.frogtest.movieguru.domain.repository.MovieRepository
import dagger.Binds
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
abstract class  RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository

}