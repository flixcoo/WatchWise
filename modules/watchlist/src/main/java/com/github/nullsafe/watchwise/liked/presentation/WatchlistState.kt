package com.github.nullsafe.watchwise.liked.presentation

import com.github.nullsafe.watchwise.core.data.database.entity.MovieDetail
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowDetail
import com.github.nullsafe.watchwise.liked.presentation.model.WatchlistTabModel

data class WatchlistState(
    val tabs: List<WatchlistTabModel> = WatchlistTabModel.createTabList(),
    val initialTabPosition: Int = 0,
    val toWatch: List<WatchlistItem> = emptyList(),
    val seen: List<WatchlistItem> = emptyList(),
    val favourites: List<WatchlistItem> = emptyList(),
    val isLoading: Boolean = true
)

sealed class WatchlistItem {
    data class Movie(val movieDetail: MovieDetail) : WatchlistItem()
    data class TvShow(val tvShowDetail: TvShowDetail) : WatchlistItem()
}

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