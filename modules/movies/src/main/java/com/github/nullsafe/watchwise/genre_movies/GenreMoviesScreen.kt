package com.github.nullsafe.watchwise.genre_movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.github.nullsafe.watchwise.compose.components.topbar.AppCenterAlignedTopAppBar
import com.github.nullsafe.watchwise.core.data.database.entity.Movie
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import com.github.nullsafe.watchwise.compose.theme.AppTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.github.nullsafe.watchwise.compose.components.cards.ItemCardHorizontal
import com.github.nullsafe.watchwise.compose.components.shimmer.shimmerBackground
import com.github.nullsafe.watchwise.core.common.helper.ImageProvider
import com.github.nullsafe.watchwise.movies.R

@Composable
fun GenreMoviesScreen(
    screenName: String,
    movies: LazyPagingItems<Movie>,
    onAction: (GenreMoviesAction) -> Unit
) {
    Scaffold(
        topBar = {
            AppCenterAlignedTopAppBar(
                title = screenName,
                onBackClick = { onAction(GenreMoviesAction.BackClick) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.colors.background.default),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies.itemCount) { index ->
                val movie = movies[index]
                movie?.let {
                    ItemCardHorizontal(
                        title = it.title,
                        description = it.overview,
                        imageUrl = ImageProvider.getImageUrl(it.posterPath),
                        rating = it.voteAverage,
                        onItemClick = { onAction(GenreMoviesAction.OpenMovieDetail(it.id)) }
                    )
                }
            }

            movies.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { ShimmerMovieItem() }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { LoadingIndicator() }
                    }

                    loadState.refresh is LoadState.Error -> {
                        item { ErrorRetryButton { retry() } }
                    }
                }
            }
        }
    }
}


@Composable
fun ShimmerMovieItem() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        repeat(5) {
            Spacer(
                modifier = Modifier
                    .height(136.dp)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shimmerBackground(RoundedCornerShape(AppTheme.dimens.radiusM))
            )
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorRetryButton(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

