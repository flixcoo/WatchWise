package com.github.nullsafe.watchwise.liked.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.nullsafe.watchwise.compose.components.cards.ItemCardHorizontal
import com.github.nullsafe.watchwise.compose.components.empty_state.EmptyStateView
import com.github.nullsafe.watchwise.compose.components.shimmer.shimmerBackground
import com.github.nullsafe.watchwise.compose.theme.AppTheme
import com.github.nullsafe.watchwise.core.common.helper.ImageProvider
import com.github.nullsafe.watchwise.liked.R
import com.github.nullsafe.watchwise.liked.presentation.compose.TabIndicator
import com.github.nullsafe.watchwise.liked.presentation.model.WatchlistTabModel
import com.github.nullsafe.watchwise.liked.presentation.model.WatchlistTabType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(position)
                    }
                }
            )

            if (state.isLoading) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ShimmerItem()
                }
            } else {
                HorizontalPager(state = pagerState) { page ->
                    when (page) {
                        WatchlistTabType.TO_WATCH.tabPosition -> {
                            WatchlistItemsList(
                                items = state.toWatch,
                                tabType = WatchlistTabType.TO_WATCH,  // Wichtig: Korrekter TabType
                                onAction = onAction
                            )
                        }

                        WatchlistTabType.SEEN.tabPosition -> {
                            WatchlistItemsList(
                                items = state.seen,
                                tabType = WatchlistTabType.SEEN,  // Wichtig: Korrekter TabType
                                onAction = onAction
                            )
                        }

                        WatchlistTabType.FAVOURITES.tabPosition -> {
                            WatchlistItemsList(
                                items = state.favourites,
                                tabType = WatchlistTabType.FAVOURITES,  // Wichtig: Korrekter TabType
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
private fun WatchlistTabs(
    tabs: List<WatchlistTabModel>,
    pagerState: PagerState,
    onClick: (position: Int) -> Unit
) {
    TabRow(
        divider = { },
        selectedTabIndex = pagerState.currentPage,
        containerColor = AppTheme.colors.background.default,
        contentColor = AppTheme.colors.type.primary, // Textfarbe der Tabs
        indicator = { tabPositions ->
            TabIndicator(tabPosition = tabPositions[pagerState.currentPage])
        }
    ) {
        tabs.forEachIndexed { index, model ->
            Tab(
                selected = model.type.tabPosition == pagerState.currentPage,
                onClick = { onClick(index) },
                text = {
                    Text(text = stringResource(id = model.type.nameRes))
                }
            )
        }
    }
}

@Composable
private fun WatchlistItemsList(
    items: List<WatchlistItem>,
    tabType: WatchlistTabType,
    onAction: (WatchlistAction) -> Unit
) {
    if (items.isEmpty()) {
        val (icon, title) = when (tabType) {
            WatchlistTabType.TO_WATCH -> Pair(
                Icons.Outlined.Visibility,  // Icon für "To Watch"
                stringResource(R.string.watchlist_empty_to_watch_message)
            )

            WatchlistTabType.SEEN -> Pair(
                Icons.Outlined.CheckCircleOutline,  // Icon für "Seen"
                stringResource(R.string.watchlist_empty_seen_message)
            )

            WatchlistTabType.FAVOURITES -> Pair(
                Icons.Outlined.FavoriteBorder,  // Icon für "Favorites"
                stringResource(R.string.watchlist_empty_favourites_message)
            )
        }

        EmptyStateView(
            modifier = Modifier.fillMaxSize(),
            icon = icon,  // Hier wird das tab-spezifische Icon verwendet
            title = title,
            action = stringResource(R.string.watchlist_empty_action),
            onClick = { onAction(WatchlistAction.BrowseMoviesAndTvShows) }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                when (item) {
                    is WatchlistItem.Movie -> {
                        ItemCardHorizontal(
                            title = item.movieDetail.title,
                            description = item.movieDetail.overview,
                            imageUrl = ImageProvider.getImageUrl(item.movieDetail.posterPath),
                            rating = item.movieDetail.voteAverage,
                            onItemClick = {
                                onAction(WatchlistAction.OpenMovieDetail(item.movieDetail.id))
                            }
                        )
                    }

                    is WatchlistItem.TvShow -> {
                        ItemCardHorizontal(
                            title = item.tvShowDetail.name,
                            description = item.tvShowDetail.overview,
                            imageUrl = ImageProvider.getImageUrl(item.tvShowDetail.posterPath),
                            rating = item.tvShowDetail.voteAverage,
                            onItemClick = {
                                onAction(WatchlistAction.OpenTvShowDetail(item.tvShowDetail.id))
                            }
                        )
                    }
                }
            }
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