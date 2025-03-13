package com.github.nullsafe.watchwise.liked.presentation

import com.github.nullsafe.watchwise.core.data.database.entity.MovieDetail
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowDetail
import com.github.nullsafe.watchwise.liked.presentation.model.WatchlistTabModel

data class WatchlistState(
    val tabs: List<WatchlistTabModel> = WatchlistTabModel.createTabList(),
    val initialTabPosition: Int = 0,
    val movies: List<MovieDetail> = emptyList(),
    val tvShows: List<TvShowDetail> = emptyList(),
    val moviesLoading: Boolean = true,
    val tvShowsLoading: Boolean = true
)

sealed interface WatchlistAction {
    data class OpenMovieDetail(val movieId: Int) : WatchlistAction
    data class OpenTvShowDetail(val tvShowId: Int) : WatchlistAction
    data object BrowseMoviesAndTvShows : WatchlistAction
}

sealed interface WatchlistEffect {
    data class NavigateToMovieDetail(val movieId: Int) : WatchlistEffect
    data class NavigateToTvShowDetail(val tvShowId: Int) : WatchlistEffect
    data object NavigateToSearch : WatchlistEffect
}