package com.frogtest.movieguru.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.frogtest.movieguru.data.cache.dao.MovieDao
import com.frogtest.movieguru.data.cache.dao.MovieDetailsDao
import com.frogtest.movieguru.data.cache.dao.MovieVideoDao
import com.frogtest.movieguru.data.cache.entity.MovieDetailsEntity
import com.frogtest.movieguru.data.cache.entity.MovieEntity
import com.frogtest.movieguru.data.cache.entity.MovieVideoEntity

@Database(entities = [MovieEntity::class, MovieDetailsEntity::class, MovieVideoEntity::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val movieDetailsDao: MovieDetailsDao
    abstract val movieVideoDao: MovieVideoDao
}