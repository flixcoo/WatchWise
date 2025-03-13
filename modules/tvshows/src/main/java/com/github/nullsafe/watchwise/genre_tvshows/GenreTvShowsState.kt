package com.github.nullsafe.watchwise.genre_tvshows

data class GenreTvShowsState(
    val genreName: String = ""
)

sealed interface GenreTvShowsAction {
    data object BackClick : GenreTvShowsAction
    data class OpenTvShowDetail(val tvShowId: Int) : GenreTvShowsAction
}

sealed interface GenreTvShowsEffect {
    data object NavigateBack : GenreTvShowsEffect
    data class NavigateToTvShowDetail(val tvShowId: Int) : GenreTvShowsEffect
}