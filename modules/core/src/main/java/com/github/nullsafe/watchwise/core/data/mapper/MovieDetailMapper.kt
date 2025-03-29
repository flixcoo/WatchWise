package com.github.nullsafe.watchwise.core.data.mapper

import com.github.nullsafe.watchwise.core.data.database.entity.Genre
import com.github.nullsafe.watchwise.core.data.database.entity.GenreType
import com.github.nullsafe.watchwise.core.data.database.entity.MovieDetail
import com.github.nullsafe.watchwise.core.data.database.entity.ProductionCompany
import com.github.nullsafe.watchwise.core.data.database.entity.ProductionCountry
import com.github.nullsafe.watchwise.core.data.dto.movie.MovieDetailDto
import javax.inject.Inject

class MovieDetailMapper @Inject constructor() {
    fun map(dto: MovieDetailDto, username: String): MovieDetail {
        return MovieDetail(
            id = dto.id,
            title = dto.title,
            originalTitle = dto.originalTitle,
            originalLanguage = dto.originalLanguage,
            overview = dto.overview,
            tagline = dto.tagline,
            budget = dto.budget,
            revenue = dto.revenue,
            runtime = dto.runtime,
            releaseDate = dto.releaseDate,
            status = dto.status,
            adult = dto.adult,
            popularity = dto.popularity,
            voteAverage = dto.voteAverage,
            voteCount = dto.voteCount,
            homepage = dto.homepage,
            backdropPath = dto.backdropPath,
            posterPath = dto.posterPath,
            video = dto.video,
            genres = mapGenres(dto),
            productionCompanies = mapProductionCompanies(dto),
            productionCountries = mapProductionCountries(dto),
            liked = false,
            creditsCasts = emptyList(),
            imdbId = dto.imdbId,
            similarMovies = emptyList(),
            username = username
        )
    }

    private fun mapGenres(dto: MovieDetailDto): List<Genre> {
        return dto.genres?.map { genreDto ->
            Genre(
                id = genreDto.id,
                name = genreDto.name.orEmpty(),
                genreType = GenreType.MOVIE,
            )
        } ?: emptyList()
    }

    private fun mapProductionCompanies(dto: MovieDetailDto): List<ProductionCompany> {
        return dto.productionCompanies?.map { companyDto ->
            ProductionCompany(
                id = companyDto.id,
                name = companyDto.name.orEmpty(),
                logoPath = companyDto.logoPath,
                originCountry = companyDto.originCountry.orEmpty()
            )
        } ?: emptyList()
    }

    private fun mapProductionCountries(dto: MovieDetailDto): List<ProductionCountry> {
        return dto.productionCountries?.map { countryDto ->
            ProductionCountry(
                name = countryDto.name.orEmpty(),
                iso = countryDto.iso.orEmpty()
            )
        } ?: emptyList()
    }
}

