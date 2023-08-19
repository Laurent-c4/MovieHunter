package com.frogtest.movieguru.di

import com.frogtest.movieguru.data.repository.AuthRepositoryImpl
import com.frogtest.movieguru.data.repository.MovieRepositoryImpl
import com.frogtest.movieguru.data.repository.UserSettingsRepositoryImpl
import com.frogtest.movieguru.domain.repository.AuthRepository
import com.frogtest.movieguru.domain.repository.MovieRepository
import com.frogtest.movieguru.domain.repository.UserSettingsRepository
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