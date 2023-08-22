package com.frogtest.movieguru.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frogtest.movieguru.data.cache.dao.MovieDao
import com.frogtest.movieguru.data.cache.dao.MovieDetailsDao
import com.frogtest.movieguru.data.cache.dao.MovieRemoteKeyDao
import com.frogtest.movieguru.data.cache.entity.movie_details.MovieDetailsEntity
import com.frogtest.movieguru.data.cache.entity.movie.MovieEntity
import com.frogtest.movieguru.data.cache.entity.movie.MovieRemoteKeyEntity

@Database(entities = [MovieEntity::class, MovieDetailsEntity::class, MovieRemoteKeyEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MovieDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val movieDetailsDao: MovieDetailsDao
    abstract val movieRemoteKeyDao: MovieRemoteKeyDao
}