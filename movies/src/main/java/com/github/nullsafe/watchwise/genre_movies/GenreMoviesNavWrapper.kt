package com.github.nullsafe.watchwise.genre_movies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.nullsafe.watchwise.core.compose.AppNavRoutes

@Composable
fun GenreMoviesNavWrapper(
    genreId: Int,
    genreName: String,
    navController: NavHostController
) {

    val viewModel = hiltViewModel<GenreMoviesViewModel, GenreMoviesViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(
                genreId = genreId,
                genreName = genreName
            )
        }
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GenreMoviesEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is GenreMoviesEffect.NavigateToMovieDetail -> {
                    navController.navigate(AppNavRoutes.MovieDetails.createRoute(effect.movieId))
                }
            }
        }
    }

    val movies = viewModel.moviesPager.collectAsLazyPagingItems()

    GenreMoviesScreen(
        screenName = viewModel.state.genreName,
        movies = movies,
        onAction = viewModel::onAction
    )
}