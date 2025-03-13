package com.github.nullsafe.watchwise.core.pager.tv_shows

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.nullsafe.watchwise.core.common.network.ResultWrapper
import com.github.nullsafe.watchwise.core.data.database.entity.TvShow
import com.github.nullsafe.watchwise.core.repository.DiscoverRepository
import kotlinx.coroutines.flow.first

class GenreTvShowsPagingSource(
    private val discoverRepository : DiscoverRepository,
    private val genreId: Int,
    private val language: String?
): PagingSource<Int, TvShow>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShow> {
        return try {
            val currentPage = params.key ?: 1

            val response = discoverRepository.discoverTvShows(
                withGenres = genreId,
                language = language,
                page = currentPage
            ).first { it is ResultWrapper.Success || it is ResultWrapper.Error }

            when (response) {
                is ResultWrapper.Success -> {
                    LoadResult.Page(
                        data = response.data,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (response.data.isEmpty()) null else currentPage + 1
                    )
                }

                is ResultWrapper.Error -> {
                    LoadResult.Error(response.throwable ?: Exception("Unknown error"))
                }

                else -> {
                    LoadResult.Error(Exception("Unexpected loading state"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TvShow>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}