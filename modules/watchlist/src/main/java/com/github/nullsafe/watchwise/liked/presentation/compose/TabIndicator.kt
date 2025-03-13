package com.github.nullsafe.watchwise.liked.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.nullsafe.watchwise.compose.theme.AppTheme

@Composable
fun TabIndicator(tabPosition: TabPosition) {
    Box(
        modifier = Modifier
            .tabIndicatorOffset(tabPosition)
            .height(2.dp)
            .padding(horizontal = AppTheme.dimens.spaceXL)
            .background(
                color = AppTheme.colors.theme.tint,
                shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
            )
    )
}