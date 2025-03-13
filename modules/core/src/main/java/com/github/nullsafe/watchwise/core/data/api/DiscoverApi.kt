package com.github.nullsafe.watchwise.core.data.api

import com.github.nullsafe.watchwise.core.data.dto.movie.MoviesDto
import com.github.nullsafe.watchwise.core.data.dto.tv.TvShowsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApi {

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("with_genres") withGenres: Int? = null
    ): MoviesDto

    @GET("discover/tv")
    suspend fun discoverTvShows(
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("with_genres") withGenres: Int? = null
    ): TvShowsDto
}
