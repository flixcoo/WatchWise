package com.github.nullsafe.watchwise.tvshows.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.nullsafe.watchwise.core.compose.AppNavRoutes
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowType

@Composable
fun TvShowsNavWrapper(
    tvShowType: TvShowType,
    tvShowId: Int?,
    navController: NavHostController
) {
    val viewModel = hiltViewModel<TvShowsViewModel, TvShowsViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(
                tvShowType = tvShowType,
                tvShowId = tvShowId
            )
        }
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TvShowsEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is TvShowsEffect.NavigateToTvShowDetail -> {
                    navController.navigate(
                        AppNavRoutes.TVShowDetails.createRoute(effect.tvShowId)
                    )
                }
            }
        }
    }

    val tvShows = viewModel.tvShowsPager.collectAsLazyPagingItems()
    TvShowsScreen(
        tvShowType = viewModel.state.tvShowType,
        tvShows = tvShows,
        onAction = viewModel::onAction
    )
}