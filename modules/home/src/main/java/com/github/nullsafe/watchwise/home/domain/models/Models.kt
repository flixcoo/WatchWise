package com.github.nullsafe.watchwise.home.domain.models

import com.github.nullsafe.watchwise.core.data.database.entity.Genre

data class GenreItem(
    val id: Int,
    val icon: String,
    val title: String
)

internal fun List<Genre>.getMovieGenreItems(): List<GenreItem> {
    return GenreItemMapper.mapGenresToGenreItems(this)
}

internal fun List<Genre>.getTvShowGenreItems(): List<GenreItem> {
    return GenreItemMapper.mapGenresToGenreItems(this)
}

object GenreItemMapper {
    private val genreIdToEmojiMap = mapOf(
        28 to "🔥",     // Action
        12 to "🏕️",     // Adventure
        16 to "🎨",     // Animation
        35 to "😂",     // Comedy
        80 to "🕵️‍♂️",     // Crime
        99 to "🎥",     // Documentary
        18 to "🎭",     // Drama
        10751 to "👨‍👩‍👧",  // Family
        14 to "🧙‍♂️",     // Fantasy
        36 to "📜",     // History
        27 to "👻",     // Horror
        10402 to "🎵",  // Music
        9648 to "🕵️",   // Mystery
        10749 to "❤️",  // Romance
        878 to "🚀",    // Science Fiction
        10770 to "📺",  // TV Movie
        53 to "🔪",     // Thriller
        10752 to "⚔️",  // War
        37 to "🤠",     // Western
        10759 to "🏹",  // Action & Adventure (TV)
        10762 to "🧒",  // Kids
        10763 to "📰",  // News
        10764 to "🎤",  // Reality
        10765 to "🌌",  // Sci-Fi & Fantasy
        10766 to "🧼",  // Soap
        10767 to "🗣️",  // Talk
        10768 to "🏛️"   // War & Politics
    )

    fun mapGenresToGenreItems(genres: List<Genre>): List<GenreItem> {
        return genres.map { genre ->
            GenreItem(
                id = genre.id,
                icon = genreIdToEmojiMap[genre.id] ?: "❓",
                title = genre.name
            )
        }
    }
}
