package com.github.nullsafe.watchwise.tvshows.presentation

import com.github.nullsafe.watchwise.core.data.database.entity.TvShowType

data class TvShowsState(
    val tvShowType: TvShowType
)

sealed interface TvShowsAction {
    data object BackClick : TvShowsAction
    data class OpenTvShowDetail(val tvShowId: Int) : TvShowsAction
}

sealed interface TvShowsEffect {
    data object NavigateBack : TvShowsEffect
    data class NavigateToTvShowDetail(val tvShowId: Int) : TvShowsEffect
}