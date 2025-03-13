package com.github.nullsafe.watchwise.liked.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nullsafe.watchwise.core.repository.MoviesRepository
import com.github.nullsafe.watchwise.core.repository.TvShowsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    moviesRepository: MoviesRepository,
    tvShowsRepository: TvShowsRepository
) : ViewModel() {

    var state by mutableStateOf(WatchlistState())
        private set

    private val _effect = Channel<WatchlistEffect>()
    val effect = _effect.receiveAsFlow()

    private val savedMovies = moviesRepository.getSavedMovies()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val savedTvShows = tvShowsRepository.getSavedTvShows()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            savedMovies.collectLatest { movies ->
                state = state.copy(
                    movies = movies
                )
                delay(300)
                state = state.copy(
                    moviesLoading = false
                )
            }
        }

        viewModelScope.launch {
            savedTvShows.collectLatest { tvShows ->
                state = state.copy(
                    tvShows = tvShows,
                    tvShowsLoading = false
                )
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

