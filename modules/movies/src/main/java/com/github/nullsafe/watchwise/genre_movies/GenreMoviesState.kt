package com.github.nullsafe.watchwise.genre_movies

data class GenreMoviesState(
    val genreName: String = ""
)

sealed interface GenreMoviesAction {
    data object BackClick : GenreMoviesAction
    data class OpenMovieDetail(val movieId: Int) : GenreMoviesAction
}

sealed interface GenreMoviesEffect {
    data object NavigateBack : GenreMoviesEffect
    data class NavigateToMovieDetail(val movieId: Int) : GenreMoviesEffect
}