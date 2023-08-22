package com.frogtest.movieguru.data.cache

import androidx.room.TypeConverter
import com.frogtest.movieguru.domain.model.movie_details.BelongsToCollection
import com.frogtest.movieguru.domain.model.movie_details.Cast
import com.frogtest.movieguru.domain.model.movie_details.Credits
import com.frogtest.movieguru.domain.model.movie_details.Crew
import com.frogtest.movieguru.domain.model.movie_details.Genre
import com.frogtest.movieguru.domain.model.movie_details.ProductionCompany
import com.frogtest.movieguru.domain.model.movie_details.ProductionCountry
import com.frogtest.movieguru.domain.model.movie_details.SpokenLanguages
import com.frogtest.movieguru.domain.model.movie_details.Videos
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromGenreIds(value: List<Int>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGenresIds(value: String): List<Int> {
        val gson = Gson()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromGenres(value: List<Genre>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGenres(value: String): List<Genre> {
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromProductionCompanies(value: List<ProductionCompany>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ProductionCompany>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toProductionCompanies(value: String): List<ProductionCompany> {
        val gson = Gson()
        val type = object : TypeToken<List<ProductionCompany>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromProductionCountries(value: List<ProductionCountry>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ProductionCountry>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toProductionCountries(value: String): List<ProductionCountry> {
        val gson = Gson()
        val type = object : TypeToken<List<ProductionCountry>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromSpokenLanguages(value: List<SpokenLanguages>): String {
        val gson = Gson()
        val type = object : TypeToken<List<SpokenLanguages>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSpokenLanguages(value: String): List<SpokenLanguages> {
        val gson = Gson()
        val type = object : TypeToken<List<SpokenLanguages>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCredits(value: Credits): String {
        val gson = Gson()
        val type = object : TypeToken<Credits>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCredits(value: String): Credits {
        val gson = Gson()
        val type = object : TypeToken<Credits>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromVideos(value: Videos): String {
        val gson = Gson()
        val type = object : TypeToken<Videos>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toVideos(value: String): Videos {
        val gson = Gson()
        val type = object : TypeToken<Videos>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromBelongsToCollection(value: BelongsToCollection?): String? {
        val gson = Gson()
        val type = object : TypeToken<BelongsToCollection?>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toBelongsToCollection(value: String?): BelongsToCollection? {
        val gson = Gson()
        val type = object : TypeToken<BelongsToCollection?>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCast(value: List<Cast>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Cast>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCast(value: String): List<Cast> {
        val gson = Gson()
        val type = object : TypeToken<List<Cast>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCrew(value: List<Crew>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Crew>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCrew(value: String): List<Crew> {
        val gson = Gson()
        val type = object : TypeToken<List<Crew>>() {}.type
        return gson.fromJson(value, type)
    }





}