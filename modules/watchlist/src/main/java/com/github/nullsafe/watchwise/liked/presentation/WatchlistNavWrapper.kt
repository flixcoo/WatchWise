package com.github.nullsafe.watchwise.liked.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.nullsafe.watchwise.core.compose.AppNavRoutes

@Composable
fun WatchlistNavWrapper(
    navController: NavHostController
) {
    val viewModel: WatchlistViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WatchlistEffect.NavigateToMovieDetail -> {
                    navController.navigate(AppNavRoutes.MovieDetails.createRoute(effect.movieId))
                }

                is WatchlistEffect.NavigateToTvShowDetail -> {
                    navController.navigate(AppNavRoutes.TVShowDetails.createRoute(effect.tvShowId))
                }

                is WatchlistEffect.NavigateToSearch -> {
                    navController.navigate(AppNavRoutes.Search.route)
                }
            }
        }
    }

    WatchlistScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}