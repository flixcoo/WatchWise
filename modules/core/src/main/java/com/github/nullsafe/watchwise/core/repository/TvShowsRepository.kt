package com.github.nullsafe.watchwise.core.repository

import androidx.paging.PagingData
import com.github.nullsafe.watchwise.core.common.network.ResultWrapper
import com.github.nullsafe.watchwise.core.data.database.entity.CreditsCast
import com.github.nullsafe.watchwise.core.data.database.entity.TvShow
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowDetail
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowType
import com.github.nullsafe.watchwise.core.data.database.entity.Video
import com.github.nullsafe.watchwise.core.data.dto.movie.ExternalIds
import kotlinx.coroutines.flow.Flow

interface TvShowsRepository {
    fun getCachedTvShows(
        tvShowType: TvShowType,
        language: String?,
        page: Int?
    ): Flow<ResultWrapper<List<TvShow>>>

    fun getTvShows(
        tvShowType: TvShowType,
        language: String?,
        page: Int?,
    ): Flow<ResultWrapper<List<TvShow>>>

    fun getTvShowsPaginated(
        tvShowType: TvShowType,
        language: String?
    ): Flow<PagingData<TvShow>>

    suspend fun getTvShowDetails(
        tvShowId: Int,
        language: String?
    ): ResultWrapper<TvShowDetail>

    fun getRecommendedTvShows(
        tvShowId: Int,
        language: String?,
        page: Int?
    ): Flow<ResultWrapper<List<TvShow>>>

    fun getSimilarTvShows(
        tvShowId: Int,
        language: String?,
        page: Int?
    ): Flow<ResultWrapper<List<TvShow>>>

    fun getSimilarOrRecommendedPaginated(
        tvShowId: Int,
        tvShowType: TvShowType,
        language: String?
    ): Flow<PagingData<TvShow>>

    fun getTvShowCredits(
        tvShowId: Int,
        language: String?
    ): Flow<ResultWrapper<List<CreditsCast>>>

    fun getTvShowVideos(tvShowId: Int, language: String?): Flow<ResultWrapper<List<Video>>>

    suspend fun getTvShowExternalIds(
        tvShowId: Int
    ): ResultWrapper<ExternalIds?>

    suspend fun saveTvShow(tvShow: TvShowDetail)

    suspend fun removeTvShow(tvShow: TvShowDetail)

    suspend fun toggleTvShowLike(tvShow: TvShowDetail)

    suspend fun isTvShowSaved(id: Int): Boolean

    fun getSavedTvShows(): Flow<List<TvShowDetail>>
}