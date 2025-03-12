package com.github.nullsafe.watchwise.liked.presentation.model

import com.github.nullsafe.watchwise.liked.R

data class WatchlistTabModel(
    val type: WatchlistTabType
) {
    companion object {
        fun createTabList() = listOf(
            WatchlistTabModel(type = WatchlistTabType.MOVIES),
            WatchlistTabModel(type = WatchlistTabType.TV_SHOWS)
        )
    }
}

enum class WatchlistTabType(val tabPosition: Int, val nameRes: Int) {
    MOVIES(tabPosition = 0, nameRes = R.string.movies),
    TV_SHOWS(tabPosition = 1, nameRes = R.string.tv_shows)
}