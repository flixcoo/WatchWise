package com.github.nullsafe.watchwise.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDetail(
    @PrimaryKey val id: Int,
    val adult: Boolean?,
    val budget: Int?,
    val genres: List<Genre>?,
    val homepage: String?,
    val imdbId: String?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    val productionCompanies: List<ProductionCompany>?,
    val productionCountries: List<ProductionCountry>?,
    val releaseDate: String?,
    val revenue: Long?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val backdropPath: String?,
    val posterPath: String?,
    val liked: Boolean,
    val isUnwatched: Boolean = false,
    val isWatched: Boolean = false,
    val isFavourite: Boolean = false,
    val creditsCasts: List<CreditsCast>,
    val similarMovies: List<Movie>
)