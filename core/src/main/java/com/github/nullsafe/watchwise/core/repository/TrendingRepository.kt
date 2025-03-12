package com.github.nullsafe.watchwise.core.repository

import androidx.paging.PagingData
import com.github.nullsafe.watchwise.core.common.network.ResultWrapper
import com.github.nullsafe.watchwise.core.data.database.entity.Movie
import com.github.nullsafe.watchwise.core.data.database.entity.MovieType
import com.github.nullsafe.watchwise.core.data.database.entity.Person
import com.github.nullsafe.watchwise.core.data.database.entity.PersonType
import com.github.nullsafe.watchwise.core.data.database.entity.TvShow
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowType
import kotlinx.coroutines.flow.Flow

interface TrendingRepository {
    fun getCachedFirstTrendingMovies(
        movieType: MovieType,
        language: String?,
        page: Int?
    ): Flow<ResultWrapper<List<Movie>>>

    fun getTrendingMovies(
        movieType: MovieType,
        language: String?,
        page: Int?
    ): Flow<ResultWrapper<List<Movie>>>

    fun getTrendingMoviesPaginated(
        movieType: MovieType,
        language: String?
    ): Flow<PagingData<Movie>>

    fun getTrendingTvShows(
        tvShowType: TvShowType,
        language: String?,
        page: Int?
    ): Flow<List<TvShow>>

    fun getTrendingPeople(
        personType: PersonType,
        language: String?,
        page: Int?
    ): Flow<List<Person>>
}