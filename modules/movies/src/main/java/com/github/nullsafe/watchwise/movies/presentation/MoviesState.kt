package com.github.nullsafe.watchwise.movies.presentation

import com.github.nullsafe.watchwise.core.data.database.entity.MovieType

data class MoviesState(
    val movieType: MovieType
)

sealed interface MoviesAction {
    data object BackClick : MoviesAction
    data class OpenMovieDetail(val movieId: Int) : MoviesAction
}

sealed interface MoviesEffect {
    data object NavigateBack : MoviesEffect
    data class NavigateToMovieDetail(val movieId: Int) : MoviesEffect
}