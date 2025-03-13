package com.github.nullsafe.watchwise.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.nullsafe.watchwise.compose.components.video_player.YouTubePlayerScreen
import com.github.nullsafe.watchwise.compose.theme.Themes
import com.github.nullsafe.watchwise.core.compose.AppNavRoutes
import com.github.nullsafe.watchwise.core.data.database.entity.MovieType
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowType
import com.github.nullsafe.watchwise.detail.moviedetail.presentation.MovieDetailsNavWrapper
import com.github.nullsafe.watchwise.detail.persondetail.presentation.PersonDetailsNavWrapper
import com.github.nullsafe.watchwise.detail.tvshowdetail.presentation.TvShowDetailsNavWrapper
import com.github.nullsafe.watchwise.genre_movies.GenreMoviesNavWrapper
import com.github.nullsafe.watchwise.genre_tvshows.GenreTvShowsNavWrapper
import com.github.nullsafe.watchwise.home.presentation.HomeScreenNavWrapper
import com.github.nullsafe.watchwise.liked.presentation.WatchlistNavWrapper
import com.github.nullsafe.watchwise.movies.presentation.MoviesNavWrapper
import com.github.nullsafe.watchwise.profile.presentation.compose.ProfileScreen
import com.github.nullsafe.watchwise.search.presentation.result.SearchNavWrapper
import com.github.nullsafe.watchwise.tvshows.presentation.TvShowsNavWrapper

@Composable
fun AppNavigation(
    navController: NavHostController,
    isDarkTheme: Boolean,
    currentTheme: Themes,
    onDarkThemeClick: () -> Unit,
    onThemeClick: (Themes) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppNavRoutes.HomeGraph.route,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { fadeOut(animationSpec = tween(200)) }
    ) {
        // Home Tab
        navigation(
            startDestination = AppNavRoutes.Home.route,
            route = AppNavRoutes.HomeGraph.route
        ) {
            composable(AppNavRoutes.Home.route) {
                HomeScreenNavWrapper(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    currentTheme = currentTheme,
                    onDarkThemeClick = onDarkThemeClick,
                    onThemeClick = onThemeClick
                )
            }
            composable(
                route = AppNavRoutes.MovieDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    MovieDetailsNavWrapper(
                        itemId = itemId,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.PersonDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    PersonDetailsNavWrapper(
                        personId = itemId,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.TVShowDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    TvShowDetailsNavWrapper(
                        tvShowId = itemId,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.YouTubePlayer.route,
                arguments = listOf(navArgument("videoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val videoId = backStackEntry.arguments?.getString("videoId")
                videoId?.let {
                    YouTubePlayerScreen(videoId = videoId) {
                        navController.popBackStack()
                    }
                }
            }
            composable(
                route = AppNavRoutes.Movies.route,
                arguments = listOf(
                    navArgument("movieId") {
                        nullable = true
                        type = NavType.StringType
                        defaultValue = "null"
                    },
                    navArgument("movieType") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val movieIdArg = backStackEntry.arguments?.getString("movieId")
                val movieId = movieIdArg?.toIntOrNull()
                val movieType =
                    backStackEntry.arguments?.getString("movieType")?.let { MovieType.valueOf(it) }

                movieType?.let { type ->
                    MoviesNavWrapper(
                        movieId = movieId,
                        movieType = type,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.TvShows.route,
                arguments = listOf(
                    navArgument("tvShowId") {
                        nullable = true
                        type = NavType.StringType
                        defaultValue = "null"
                    },
                    navArgument("tvShowType") {
                        nullable = true
                        type = NavType.StringType
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("tvShowId")?.toIntOrNull()
                val tvShowType = backStackEntry.arguments?.getString("tvShowType")
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { TvShowType.valueOf(it) }

                tvShowType?.let { type ->
                    TvShowsNavWrapper(
                        tvShowId = itemId,
                        tvShowType = type,
                        navController = navController
                    )
                }

            }

            composable(
                route = AppNavRoutes.MoviesByGenre.route,
                arguments = listOf(
                    navArgument("genreId") { type = NavType.IntType },
                    navArgument("genreName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val genreId = backStackEntry.arguments?.getInt("genreId") ?: 0
                val genreName = backStackEntry.arguments?.getString("genreName").orEmpty()

                GenreMoviesNavWrapper(
                    genreId = genreId,
                    genreName = genreName,
                    navController = navController
                )
            }

            composable(
                route = AppNavRoutes.TvShowsByGenre.route,
                arguments = listOf(
                    navArgument("genreId") { type = NavType.IntType },
                    navArgument("genreName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val genreId = backStackEntry.arguments?.getInt("genreId") ?: 0
                val genreName = backStackEntry.arguments?.getString("genreName").orEmpty()

                GenreTvShowsNavWrapper(
                    genreId = genreId,
                    genreName = genreName,
                    navController = navController
                )
            }

        }

        // Search Tab
        navigation(
            startDestination = AppNavRoutes.Search.route,
            route = AppNavRoutes.SearchGraph.route
        ) {
            composable(AppNavRoutes.Search.route) {
                SearchNavWrapper(navController = navController)
            }
            composable(
                route = AppNavRoutes.MovieDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    MovieDetailsNavWrapper(
                        itemId = itemId,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.PersonDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    PersonDetailsNavWrapper(
                        personId = itemId,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.TVShowDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    TvShowDetailsNavWrapper(
                        tvShowId = itemId,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.Movies.route,
                arguments = listOf(
                    navArgument("movieId") {
                        nullable = true
                        type = NavType.StringType
                        defaultValue = "null"
                    },
                    navArgument("movieType") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val movieIdArg = backStackEntry.arguments?.getString("movieId")
                val movieId = movieIdArg?.toIntOrNull()
                val movieType =
                    backStackEntry.arguments?.getString("movieType")?.let { MovieType.valueOf(it) }

                movieType?.let { type ->
                    MoviesNavWrapper(
                        movieId = movieId,
                        movieType = type,
                        navController = navController
                    )
                }
            }
        }

        // Watchlist Tab
        navigation(
            startDestination = AppNavRoutes.Watchlist.route,
            route = AppNavRoutes.WatchlistGraph.route
        ) {
            composable(AppNavRoutes.Watchlist.route) {
                WatchlistNavWrapper(navController)
            }
            composable(
                route = AppNavRoutes.MovieDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    MovieDetailsNavWrapper(
                        itemId = itemId,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.PersonDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    PersonDetailsNavWrapper(
                        personId = itemId,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.TVShowDetails.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    TvShowDetailsNavWrapper(
                        tvShowId = itemId,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.TvShows.route,
                arguments = listOf(
                    navArgument("tvShowId") {
                        nullable = true
                        type = NavType.StringType
                        defaultValue = "null"
                    },
                    navArgument("tvShowType") {
                        nullable = true
                        type = NavType.StringType
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("tvShowId")?.toIntOrNull()
                val tvShowType = backStackEntry.arguments?.getString("tvShowType")
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { TvShowType.valueOf(it) }

                tvShowType?.let { type ->
                    TvShowsNavWrapper(
                        tvShowId = itemId,
                        tvShowType = type,
                        navController = navController
                    )
                }
            }
            composable(
                route = AppNavRoutes.Movies.route,
                arguments = listOf(
                    navArgument("movieId") {
                        nullable = true
                        type = NavType.StringType
                        defaultValue = "null"
                    },
                    navArgument("movieType") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val movieIdArg = backStackEntry.arguments?.getString("movieId")
                val movieId = movieIdArg?.toIntOrNull()
                val movieType =
                    backStackEntry.arguments?.getString("movieType")?.let { MovieType.valueOf(it) }

                movieType?.let { type ->
                    MoviesNavWrapper(
                        movieId = movieId,
                        movieType = type,
                        navController = navController
                    )
                }
            }
        }

        // Profile Tab
        navigation(
            startDestination = AppNavRoutes.Profile.route,
            route = AppNavRoutes.ProfileGraph.route
        ) {
            composable(AppNavRoutes.Profile.route) { ProfileScreen(navController = navController) }
        }
    }
}
