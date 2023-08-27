package com.c4entertainment.moviehunter.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.c4entertainment.moviehunter.data.cache.dao.MovieDao
import com.c4entertainment.moviehunter.data.cache.dao.MovieDetailsDao
import com.c4entertainment.moviehunter.data.cache.dao.MovieRemoteKeyDao
import com.c4entertainment.moviehunter.data.cache.dao.MovieSearchDao
import com.c4entertainment.moviehunter.data.cache.dao.MovieSearchRemoteKeyDao
import com.c4entertainment.moviehunter.data.cache.entity.movie_details.MovieDetailsEntity
import com.c4entertainment.moviehunter.data.cache.entity.movie.MovieEntity
import com.c4entertainment.moviehunter.data.cache.entity.movie.MovieRemoteKeyEntity
import com.c4entertainment.moviehunter.data.cache.entity.search.MovieSearchEntity
import com.c4entertainment.moviehunter.data.cache.entity.search.MovieSearchRemoteKeyEntity

@Database(entities = [MovieEntity::class, MovieDetailsEntity::class, MovieSearchEntity::class, MovieRemoteKeyEntity::class, MovieSearchRemoteKeyEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MovieDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val movieSearchDao: MovieSearchDao
    abstract val movieDetailsDao: MovieDetailsDao
    abstract val movieRemoteKeyDao: MovieRemoteKeyDao
    abstract val movieSearchRemoteKeyDao: MovieSearchRemoteKeyDao
}