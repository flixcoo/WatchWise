package com.github.nullsafe.watchwise.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import com.github.nullsafe.watchwise.R
import com.github.nullsafe.watchwise.compose.components.navigation.BottomNavItem
import com.github.nullsafe.watchwise.core.compose.AppNavRoutes

val bottomNavItems = listOf(
    BottomNavItem(
        textRes = R.string.nav_home,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Default.Home,
        route = AppNavRoutes.Home.route
    ),
    BottomNavItem(
        textRes = R.string.nav_search,
        icon = Icons.Outlined.Search,
        selectedIcon = Icons.Default.Search,
        route = AppNavRoutes.Search.route
    ),
    BottomNavItem(
        textRes = R.string.nav_watchlist,
        icon = Icons.Outlined.Subscriptions,
        selectedIcon = Icons.Default.Subscriptions,
        route = AppNavRoutes.Watchlist.route
    ),
    BottomNavItem(
        textRes = R.string.nav_profile,
        icon = Icons.Outlined.Person,
        selectedIcon = Icons.Default.Person,
        route = AppNavRoutes.Profile.route
    )
)
