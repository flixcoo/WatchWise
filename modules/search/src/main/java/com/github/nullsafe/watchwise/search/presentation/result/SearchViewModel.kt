package com.github.nullsafe.watchwise.search.presentation.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nullsafe.watchwise.core.common.network.ResultWrapper
import com.github.nullsafe.watchwise.core.data.database.entity.MediaType
import com.github.nullsafe.watchwise.core.data.database.entity.MovieType
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowType
import com.github.nullsafe.watchwise.core.repository.SearchRepository
import com.github.nullsafe.watchwise.core.repository.TrendingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val trendingRepository: TrendingRepository
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _effect = Channel<SearchEffect>()
    val effect = _effect.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        fetchPopularSearches()
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.UpdateQuery -> {
                state = state.copy(query = action.query)

                if (action.query.isNotBlank()) {
                    performSearch(action.query)
                } else {
                    fetchPopularSearches()
                }
            }

            is SearchAction.ClearQuery -> {
                state = state.copy(
                    query = "",
                    searchResult = emptyList(),
                    isError = false
                )
                fetchPopularSearches()
            }

            is SearchAction.OpenMovieDetail -> {
                viewModelScope.launch {
                    _effect.send(SearchEffect.NavigateToMovieDetail(action.movieId))
                }
            }

            is SearchAction.OpenTvShowDetail -> {
                viewModelScope.launch {
                    _effect.send(SearchEffect.NavigateToTvShowDetail(action.tvShowId))
                }
            }

            is SearchAction.OpenPersonDetail -> {
                viewModelScope.launch {
                    _effect.send(SearchEffect.NavigateToPersonDetail(action.personId))
                }
            }
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            state = state.copy(isLoading = true, isError = false)
            delay(300)

            try {
                val results = searchRepository.getSearchMulti(query = query, language = null)
                    .filter { item ->
                        when (item.mediaType) {
                            MediaType.MOVIE -> item.posterPath != null
                            MediaType.TV -> item.posterPath != null
                            MediaType.PERSON -> item.profilePath != null
                            else -> item.posterPath != null && item.profilePath != null
                        }
                    }

                state = state.copy(
                    searchResult = results,
                    isLoading = false
                )
            } catch (e: Exception) {
                state = state.copy(
                    searchResult = emptyList(),
                    isLoading = false,
                    isError = true
                )
            }
        }
    }

    private fun fetchPopularSearches() {
        viewModelScope.launch {
            try {
                // Combine movies and TV shows flows
                combine(
                    trendingRepository.getTrendingMovies(
                        MovieType.TRENDING_WEEK,
                        language = null,
                        page = null
                    ),
                    trendingRepository.getTrendingTvShows(
                        TvShowType.TRENDING_WEEK,
                        language = null,
                        page = null
                    )
                ) { moviesResult, tvShows ->
                    val movieTitles = if (moviesResult is ResultWrapper.Success) {
                        moviesResult.data.sortedBy { it.popularity }.mapNotNull { it.title }
                    } else emptyList()

                    val tvShowTitles = tvShows.sortedBy { it.popularity }.mapNotNull { it.name }

                    (movieTitles + tvShowTitles)
                }.collect { combinedTitles ->
                    state = state.copy(
                        popularSearches = combinedTitles.take(26),
                        searchResult = emptyList(),
                        isError = false
                    )
                }
            } catch (e: Exception) {
                state = state.copy(
                    popularSearches = emptyList(),
                    isError = true
                )
            }
        }
    }
}
