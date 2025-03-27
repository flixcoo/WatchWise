package com.github.nullsafe.watchwise.liked.presentation.model

import com.github.nullsafe.watchwise.liked.R

data class WatchlistTabModel(
    val type: WatchlistTabType
) {
    companion object {
        fun createTabList() = listOf(
            WatchlistTabModel(type = WatchlistTabType.TO_WATCH),
            WatchlistTabModel(type = WatchlistTabType.SEEN),
            WatchlistTabModel(type = WatchlistTabType.FAVOURITES)
        )
    }
}

enum class WatchlistTabType(val tabPosition: Int, val nameRes: Int) {
    TO_WATCH(tabPosition = 0, nameRes = R.string.to_watch),
    SEEN(tabPosition = 1, nameRes = R.string.seen),
    FAVOURITES(tabPosition = 2, nameRes = R.string.favourites)
}