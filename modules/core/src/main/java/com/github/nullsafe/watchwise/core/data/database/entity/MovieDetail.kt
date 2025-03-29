package com.github.nullsafe.watchwise.core.data.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["id", "username"])
data class MovieDetail(
    val id: Int,
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
    val username: String,
    val liked: Boolean = false,
    val unwatched: Boolean = false,
    val watched: Boolean = false,
    val creditsCasts: List<CreditsCast>,
    val similarMovies: List<Movie>
)