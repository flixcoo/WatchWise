package com.github.nullsafe.watchwise.liked.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.github.nullsafe.watchwise.compose.theme.AppTheme
import com.github.nullsafe.watchwise.liked.R
import com.github.nullsafe.watchwise.liked.presentation.compose.AppTab
import com.github.nullsafe.watchwise.liked.presentation.compose.TabComponentModel
import com.github.nullsafe.watchwise.liked.presentation.compose.TabIndicator
import com.github.nullsafe.watchwise.liked.presentation.model.WatchlistTabModel
import com.github.nullsafe.watchwise.liked.presentation.model.WatchlistTabType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.TabRow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import com.github.nullsafe.watchwise.compose.components.cards.ItemCardHorizontal
import com.github.nullsafe.watchwise.compose.components.empty_state.EmptyStateView
import com.github.nullsafe.watchwise.compose.components.shimmer.shimmerBackground
import com.github.nullsafe.watchwise.core.common.helper.ImageProvider
import com.github.nullsafe.watchwise.core.data.database.entity.MovieDetail
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    state: WatchlistState,
    onAction: (WatchlistAction) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val tabs by remember { mutableStateOf(state.tabs) }

    val pagerState: PagerState = rememberPagerState(
        initialPage = state.initialTabPosition,
        pageCount = { tabs.size }
    )

    val lazyListStateMovies = rememberLazyListState()
    val lazyListStateTvShows = rememberLazyListState()

    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors().copy(
                    titleContentColor = AppTheme.colors.type.secondary,
                    containerColor = AppTheme.colors.theme.tintSelection,
                    scrolledContainerColor = AppTheme.colors.theme.tintSelection
                ),
                title = {
                    Text(
                        text = stringResource(R.string.nav_watchlist),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colors.type.secondary
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(AppTheme.colors.background.default)
        ) {

            WatchlistTabs(
                tabs = tabs,
                pagerState = pagerState,
                onClick = { position ->
                    coroutineScope.launch { pagerState.scrollToPage(position) }
                }
            )
            if (state.moviesLoading || state.tvShowsLoading) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ShimmerItem()
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    when (page) {
                        WatchlistTabType.MOVIES.tabPosition -> {
                            MoviesList(
                                lazyListState = lazyListStateMovies,
                                movies = state.movies,
                                onAction = onAction
                            )
                        }

                        WatchlistTabType.TV_SHOWS.tabPosition -> {
                            TvShowsList(
                                lazyListState = lazyListStateTvShows,
                                tvShows = state.tvShows,
                                onAction = onAction
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoviesList(
    lazyListState: LazyListState,
    movies: List<MovieDetail>,
    onAction: (WatchlistAction) -> Unit
) {
    if (movies.isEmpty()) {
        EmptyStateView(
            modifier = Modifier.fillMaxSize(),
            icon = Icons.Outlined.BookmarkBorder,
            title = stringResource(id = R.string.watchlist_empty_message_movies),
            action = stringResource(id = R.string.watchlist_empty_action),
            onClick = { onAction(WatchlistAction.BrowseMoviesAndTvShows) }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = movies,
                key = { movie -> movie.id }
            ) { movie ->
                ItemCardHorizontal(
                    title = movie.title,
                    description = movie.overview,
                    imageUrl = ImageProvider.getImageUrl(movie.posterPath),
                    rating = movie.voteAverage,
                    onItemClick = {
                        onAction(WatchlistAction.OpenMovieDetail(movie.id))
                    }
                )
            }
        }
    }
}

@Composable
private fun TvShowsList(
    lazyListState: LazyListState,
    tvShows: List<TvShowDetail>,
    onAction: (WatchlistAction) -> Unit
) {
    if (tvShows.isEmpty()) {
        EmptyStateView(
            modifier = Modifier.fillMaxSize(),
            icon = Icons.Outlined.BookmarkBorder,
            title = stringResource(id = R.string.watchlist_empty_message_tv_shows),
            action = stringResource(id = R.string.watchlist_empty_action),
            onClick = { onAction(WatchlistAction.BrowseMoviesAndTvShows) }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = tvShows,
                key = { tvShows -> tvShows.id }
            ) { tvShows ->
                ItemCardHorizontal(
                    title = tvShows.name,
                    description = tvShows.overview,
                    imageUrl = ImageProvider.getImageUrl(tvShows.posterPath),
                    rating = tvShows.voteAverage,
                    onItemClick = {
                        onAction(WatchlistAction.OpenTvShowDetail(tvShows.id))
                    }
                )
            }
        }
    }
}

@Composable
private fun WatchlistTabs(
    tabs: List<WatchlistTabModel>,
    pagerState: PagerState,
    onClick: (position: Int) -> Unit
) {
    TabRow(
        divider = { },
        selectedTabIndex = pagerState.targetPage,
        indicator = { tabPositions ->
            TabIndicator(tabPosition = tabPositions[pagerState.currentPage])
        }
    ) {
        tabs.mapIndexed { index, model ->
            TabComponentModel(
                text = stringResource(id = model.type.nameRes),
                selected = model.type.tabPosition == pagerState.currentPage,
                onClick = { onClick(index) }
            )
        }.forEach { tabModel ->
            AppTab(tabModel = tabModel)
        }
    }
}

@Composable
private fun ShimmerItem() {
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