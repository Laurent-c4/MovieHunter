package com.frogtest.movieguru.di

import android.content.Context
import androidx.room.Room
import com.frogtest.movieguru.MovieApp
import com.frogtest.movieguru.data.cache.MovieDatabase
import com.frogtest.movieguru.data.network.api.TMDBMovieAPI
import com.frogtest.movieguru.data.repository.AuthRepositoryImpl
import com.frogtest.movieguru.domain.repository.AuthRepository
import com.frogtest.movieguru.util.ConnectivityObserver
import com.frogtest.movieguru.util.NetworkConnectivityObserver
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
        fun provideMovieApi2(): TMDBMovieAPI {
        return Retrofit.Builder()
            .baseUrl(TMDBMovieAPI.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TMDBMovieAPI::class.java)
    }


    @Provides
    fun provideAuthRepository(@ApplicationContext context: Context): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth,
        context = context,
    )

    @Provides
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

}