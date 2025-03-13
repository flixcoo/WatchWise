package com.github.nullsafe.watchwise.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.nullsafe.watchwise.compose.theme.Themes
import com.github.nullsafe.watchwise.core.compose.AppNavRoutes
import com.github.nullsafe.watchwise.home.presentation.screen.HomeScreen
import com.github.nullsafe.watchwise.home.presentation.state.HomeEffect
import com.github.nullsafe.watchwise.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreenNavWrapper(
    navController: NavHostController,
    isDarkTheme: Boolean,
    currentTheme: Themes,
    onDarkThemeClick: () -> Unit,
    onThemeClick: (theme: Themes) -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToMovieDetail -> {
                    navController.navigate(
                        AppNavRoutes.MovieDetails.createRoute(effect.movieId)
                    )
                }

                is HomeEffect.NavigateToTvShowDetail -> {
                    navController.navigate(
                        AppNavRoutes.TVShowDetails.createRoute(effect.tvShowId)
                    )
                }

                is HomeEffect.NavigateToPersonDetail -> {
                    navController.navigate(
                        AppNavRoutes.PersonDetails.createRoute(effect.personId)
                    )
                }

                is HomeEffect.NavigateToGenreMovies -> {
                    navController.navigate(
                        AppNavRoutes.MoviesByGenre.createRoute(
                            genreId = effect.genreId,
                            genreName = effect.genreName
                        )
                    )
                }

                is HomeEffect.NavigateToGenreTvShows -> {
                    navController.navigate(
                        AppNavRoutes.TvShowsByGenre.createRoute(
                            genreId = effect.genreId,
                            genreName = effect.genreName
                        )
                    )
                }

                is HomeEffect.ChangeAppColor -> {
                    onThemeClick(effect.selectedTheme)
                }

                is HomeEffect.NavigateToMovies -> {
                    navController.navigate(
                        AppNavRoutes.Movies.createRoute(
                            movieId = null,
                            movieType = effect.movieType
                        )
                    )
                }

                is HomeEffect.NavigateToTvShows -> {
                    navController.navigate(
                        AppNavRoutes.TvShows.createRoute(
                            tvShowId = null,
                            tvShowType = effect.tvShowType
                        )
                    )
                }
            }
        }
    }

    HomeScreen(
        currentTheme = currentTheme,
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}