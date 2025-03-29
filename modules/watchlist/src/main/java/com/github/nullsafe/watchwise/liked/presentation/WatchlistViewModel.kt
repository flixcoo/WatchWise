package com.github.nullsafe.watchwise.liked.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nullsafe.watchwise.core.data.database.dao.MovieDetailDao
import com.github.nullsafe.watchwise.core.data.database.dao.TvShowDetailDao
import com.github.nullsafe.watchwise.core.data.database.entity.MovieDetail
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowDetail
import com.github.nullsafe.watchwise.core.data.datastore.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    movieDao: MovieDetailDao,
    tvShowDao: TvShowDetailDao,
    userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    var state by mutableStateOf(WatchlistState())
        private set

    private val _effect = Channel<WatchlistEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            val username = userPreferencesManager.userPreferencesFlow.first().userName
            Log.d("WatchlistViewModel", "Loaded username from preferences: $username")

            val moviesToWatch = movieDao.getMoviesUnwatch(username)
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

            val moviesSeen = movieDao.getMoviesWatched(username)
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

            val moviesFavourites = movieDao.getMoviesFavourites(username)
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

            val tvShowsToWatch = tvShowDao.getTvShowsUnwatched(username)
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

            val tvShowsSeen = tvShowDao.getTvShowsWatched(username)
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

            val tvShowsFavourites = tvShowDao.getTvShowsFavourites(username)
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

            try {
                withTimeout(5000) {
                    combine(
                        moviesToWatch,
                        moviesSeen,
                        moviesFavourites,
                        tvShowsToWatch,
                        tvShowsSeen,
                        tvShowsFavourites
                    ) { flows ->
                        val toWatchMovies = flows[0] as List<MovieDetail>
                        val seenMovies = flows[1] as List<MovieDetail>
                        val favouriteMovies = flows[2] as List<MovieDetail>
                        val toWatchTvShows = flows[3] as List<TvShowDetail>
                        val seenTvShows = flows[4] as List<TvShowDetail>
                        val favouriteTvShows = flows[5] as List<TvShowDetail>

                        Log.d("WatchlistViewModel", "Watchlist: loaded for $username")

                        state = state.copy(
                            toWatch = toWatchMovies.map { WatchlistItem.Movie(it) } + toWatchTvShows.map { WatchlistItem.TvShow(it) },
                            seen = seenMovies.map { WatchlistItem.Movie(it) } + seenTvShows.map { WatchlistItem.TvShow(it) },
                            favourites = favouriteMovies.map { WatchlistItem.Movie(it) } + favouriteTvShows.map { WatchlistItem.TvShow(it) },
                            isLoading = false
                        )
                    }.collectLatest {
                        Log.d("WatchlistViewModel", "Data loaded successfully")
                    }
                }
            } catch (e: TimeoutCancellationException) {
                Log.d("WatchlistViewModel", "Timeout: Combine block did not execute")
                state = state.copy(isLoading = false)
            } catch (e: Exception) {
                Log.e("WatchlistViewModel", "Error loading watchlist items", e)
                state = state.copy(isLoading = false)
            }
        }
    }

    fun onAction(action: WatchlistAction) {
        when (action) {
            is WatchlistAction.OpenMovieDetail -> {
                viewModelScope.launch { _effect.send(WatchlistEffect.NavigateToMovieDetail(action.movieId)) }
            }

            is WatchlistAction.OpenTvShowDetail -> {
                viewModelScope.launch { _effect.send(WatchlistEffect.NavigateToTvShowDetail(action.tvShowId)) }
            }

            is WatchlistAction.BrowseMoviesAndTvShows -> {
                viewModelScope.launch { _effect.send(WatchlistEffect.NavigateToSearch) }
            }
        }
    }
}