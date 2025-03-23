package com.github.nullsafe.watchwise.detail.moviedetail.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nullsafe.watchwise.core.common.network.ResultWrapper
import com.github.nullsafe.watchwise.core.repository.MoviesRepository
import com.github.nullsafe.watchwise.detail.moviedetail.presentation.state.MovieDetailsAction
import com.github.nullsafe.watchwise.detail.moviedetail.presentation.state.MovieDetailsEffect
import com.github.nullsafe.watchwise.detail.moviedetail.presentation.state.MovieDetailsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest

import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MovieDetailsViewModel.Factory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    var state by mutableStateOf(MovieDetailsState())
        private set

    private val _effect = Channel<MovieDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        fetchMovieDetails()
    }

    private fun fetchMovieDetails() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, isError = false)

            val movieDetailsResult = moviesRepository.getMovieDetails(movieId, null)

            val movie = (movieDetailsResult as? ResultWrapper.Success)?.data
            if(movie != null) {
                val isUnwatched = moviesRepository.isMovieUnwatched(movie.id)
                val isWatched = moviesRepository.isMovieWatched(movie.id)
                val isSaved = moviesRepository.isMovieSaved(movie.id)
                state = state.copy(
                    movie = movie.copy(liked = isSaved, isWatched = isWatched, isUnwatched = isUnwatched),
                    isError = false
                )
            } else {
                state = state.copy(isError = true)
            }

            val externalIds = (moviesRepository.getMovieExternalIds(
                movieId,
                null
            ) as? ResultWrapper.Success)?.data
            externalIds?.let { socialIds ->
                state = state.copy(
                    movieSocialIds = state.movieSocialIds.copy(
                        wikidataId = socialIds.wikidataId,
                        facebookId = socialIds.facebookId,
                        instagramId = socialIds.instagramId,
                        twitterId = socialIds.twitterId
                    )
                )
            }

            // Collect Similar Movies as Flow
            launch {
                moviesRepository.getSimilarMovies(movieId, null, null)
                    .collectLatest { result ->
                        state = state.copy(
                            similarMovies = (result as? ResultWrapper.Success)?.data ?: emptyList()
                        )
                    }
            }

            // Collect Recommended Movies as Flow
            launch {
                moviesRepository.getRecommendedMovies(movieId, null, null)
                    .collectLatest { result ->
                        state = state.copy(
                            recommendedMovies = (result as? ResultWrapper.Success)?.data
                                ?: emptyList()
                        )
                    }
            }

            // Collect Videos as Flow
            launch {
                moviesRepository.getMovieVideos(movieId, null)
                    .collectLatest { result ->
                        state = state.copy(
                            videos = (result as? ResultWrapper.Success)?.data ?: emptyList()
                        )
                    }
            }

            // Collect Casts as Flow
            launch {
                moviesRepository.getMovieCredits(movieId, null)
                    .collectLatest { result ->
                        state = state.copy(
                            casts = (result as? ResultWrapper.Success)?.data ?: emptyList()
                        )
                    }
            }

            state = state.copy(isLoading = false)
        }
    }

    fun onAction(action: MovieDetailsAction) {
        when (action) {
            is MovieDetailsAction.BackClick -> {
                viewModelScope.launch { _effect.send(MovieDetailsEffect.NavigateBack) }
            }

            is MovieDetailsAction.OpenMovieDetail -> {
                viewModelScope.launch {
                    _effect.send(MovieDetailsEffect.NavigateToMovieDetail(action.movieId))
                }
            }

            is MovieDetailsAction.OpenPersonDetail -> {
                viewModelScope.launch { _effect.send(MovieDetailsEffect.NavigateToPerson(action.personId)) }
            }

            is MovieDetailsAction.OpenVideo -> {
                viewModelScope.launch { _effect.send(MovieDetailsEffect.NavigateToVideo(action.videoId)) }
            }

            is MovieDetailsAction.OpenMoviesByType -> {
                viewModelScope.launch {
                    _effect.send(
                        MovieDetailsEffect.NavigateToMovies(
                            movieId = movieId,
                            movieType = action.movieType
                        )
                    )
                }
            }

            is MovieDetailsAction.ToggleLike -> {
                viewModelScope.launch {
                    state.movie?.let { movie ->
                        moviesRepository.toggleMovieLike(movie)
                        state = state.copy(movie = movie.copy(liked = !movie.liked))
                    }
                }
            }

            is MovieDetailsAction.ToggleWatched -> {
                viewModelScope.launch {
                    state.movie?.let { movie ->
                        moviesRepository.toggleMovieWatched(movie)
                        state = state.copy(movie = movie.copy(isWatched = !movie.isWatched))
                    }
                }
            }

            is MovieDetailsAction.ToggleUnwatched -> {
                viewModelScope.launch {
                    state.movie?.let { movie ->
                        moviesRepository.toggleMovieUnwatched(movie)
                        state = state.copy(movie = movie.copy(isUnwatched = !movie.isUnwatched))
                    }
                }
            }

            is MovieDetailsAction.RetryFetch -> {
                fetchMovieDetails()
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(movieId: Int): MovieDetailsViewModel
    }
}