package com.github.nullsafe.watchwise.genre_movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.nullsafe.watchwise.core.data.database.entity.Movie
import com.github.nullsafe.watchwise.core.repository.DiscoverRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GenreMoviesViewModel.Factory::class)
class GenreMoviesViewModel @AssistedInject constructor(
    @Assisted private val genreId: Int,
    @Assisted private val genreName: String,
    discoverRepository: DiscoverRepository
) : ViewModel() {

    var state by mutableStateOf(GenreMoviesState(genreName = genreName))
        private set

    private val _effect = Channel<GenreMoviesEffect>()
    val effect = _effect.receiveAsFlow()

    val moviesPager: Flow<PagingData<Movie>> =
        discoverRepository.getMoviesPaginated(genreId = genreId, language = null)
            .cachedIn(viewModelScope)

    fun onAction(action: GenreMoviesAction) {
        when (action) {
            is GenreMoviesAction.BackClick -> {
                viewModelScope.launch { _effect.send(GenreMoviesEffect.NavigateBack) }
            }

            is GenreMoviesAction.OpenMovieDetail -> {
                viewModelScope.launch { _effect.send(GenreMoviesEffect.NavigateToMovieDetail(action.movieId)) }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            genreId: Int,
            genreName: String
        ): GenreMoviesViewModel
    }
}

