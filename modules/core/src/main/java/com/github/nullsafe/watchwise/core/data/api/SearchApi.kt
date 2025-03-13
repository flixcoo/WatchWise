package com.github.nullsafe.watchwise.core.data.api

import com.github.nullsafe.watchwise.core.data.dto.movie.MoviesDto
import com.github.nullsafe.watchwise.core.data.dto.search.SearchMultiDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("include_adult") adult: String? = null,
        @Query("region") region: String? = null,
        @Query("year") year: Int? = null
    ): MoviesDto

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("include_adult") adult: String? = null,
        @Query("region") region: String? = null
    ): SearchMultiDto
}