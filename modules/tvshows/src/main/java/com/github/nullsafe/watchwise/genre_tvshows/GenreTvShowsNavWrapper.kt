package com.github.nullsafe.watchwise.genre_tvshows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.nullsafe.watchwise.core.compose.AppNavRoutes

@Composable
fun GenreTvShowsNavWrapper(
    genreId: Int,
    genreName: String,
    navController: NavHostController
) {

    val viewModel = hiltViewModel<GenreTvShowsViewModel, GenreTvShowsViewModel.Factory>(
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
                is GenreTvShowsEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is GenreTvShowsEffect.NavigateToTvShowDetail -> {
                    navController.navigate(AppNavRoutes.TVShowDetails.createRoute(effect.tvShowId))
                }
            }
        }
    }

    val tvShows = viewModel.tvShowsPager.collectAsLazyPagingItems()

    GenreTvShowsScreen(
        screenName = viewModel.state.genreName,
        tvShows = tvShows,
        onAction = viewModel::onAction
    )
}