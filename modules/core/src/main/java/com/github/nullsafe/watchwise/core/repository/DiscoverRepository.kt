package com.github.nullsafe.watchwise.core.repository

import androidx.paging.PagingData
import com.github.nullsafe.watchwise.core.common.network.ResultWrapper
import com.github.nullsafe.watchwise.core.data.database.entity.Movie
import com.github.nullsafe.watchwise.core.data.database.entity.TvShow
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {
    fun discoverMovies(
        language: String?,
        page: Int?,
        withGenres: Int?
    ): Flow<ResultWrapper<List<Movie>>>

    fun discoverTvShows(
        language: String?,
        page: Int?,
        withGenres: Int?
    ): Flow<ResultWrapper<List<TvShow>>>

    fun getMoviesPaginated(
        genreId: Int,
        language: String?
    ): Flow<PagingData<Movie>>

    fun getTvShowsPaginated(
        genreId: Int,
        language: String?
    ): Flow<PagingData<TvShow>>
}
