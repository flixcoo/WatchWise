package com.github.nullsafe.watchwise.liked.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.nullsafe.watchwise.compose.theme.AppTheme

@Composable
fun AppTab(tabModel: TabComponentModel) {
    Tab(
        modifier = Modifier.background(AppTheme.colors.theme.tintSelection),
        selected = tabModel.selected,
        onClick = tabModel.onClick,
        selectedContentColor = AppTheme.colors.type.secondary,
        unselectedContentColor = AppTheme.colors.type.secondary,
        text = {
            Text(
                text = tabModel.text,
                style = AppTheme.typography.caption1Medium,
                color = AppTheme.colors.type.secondary
            )
        }
    )
}