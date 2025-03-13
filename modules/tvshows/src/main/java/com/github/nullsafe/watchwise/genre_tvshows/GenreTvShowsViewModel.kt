package com.github.nullsafe.watchwise.genre_tvshows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.nullsafe.watchwise.core.data.database.entity.TvShow
import com.github.nullsafe.watchwise.core.repository.DiscoverRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GenreTvShowsViewModel.Factory::class)
class GenreTvShowsViewModel @AssistedInject constructor(
    @Assisted private val genreId: Int,
    @Assisted private val genreName: String,
    discoverRepository: DiscoverRepository
) : ViewModel() {

    var state by mutableStateOf(GenreTvShowsState(genreName = genreName))
        private set

    private val _effect = Channel<GenreTvShowsEffect>()
    val effect = _effect.receiveAsFlow()

    val tvShowsPager: Flow<PagingData<TvShow>> =
        discoverRepository.getTvShowsPaginated(genreId = genreId, language = null)
            .cachedIn(viewModelScope)

    fun onAction(action: GenreTvShowsAction) {
        when (action) {
            is GenreTvShowsAction.BackClick -> {
                viewModelScope.launch { _effect.send(GenreTvShowsEffect.NavigateBack) }
            }

            is GenreTvShowsAction.OpenTvShowDetail -> {
                viewModelScope.launch {
                    _effect.send(
                        GenreTvShowsEffect.NavigateToTvShowDetail(
                            action.tvShowId
                        )
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            genreId: Int,
            genreName: String
        ): GenreTvShowsViewModel
    }
}