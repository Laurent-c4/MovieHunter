package com.c4entertainment.moviehunter.di

import com.c4entertainment.moviehunter.data.repository.MovieRepositoryImpl
import com.c4entertainment.moviehunter.data.repository.UserSettingsRepositoryImpl
import com.c4entertainment.moviehunter.domain.repository.MovieRepository
import com.c4entertainment.moviehunter.domain.repository.UserSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class  MovieRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository

    @Binds
    abstract fun bindsUserDataRepository(
        userDataRepository: UserSettingsRepositoryImpl,
    ): UserSettingsRepository

//    @Binds
//    @Singleton
//    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

}