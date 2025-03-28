package com.github.nullsafe.watchwise.profile.presentation.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.nullsafe.watchwise.compose.theme.AppTheme


@Composable
fun RuleCheck(text: String, passed: Boolean) {
    val color = if (passed) Color(0xFF4CAF50) else Color.Red
    val icon = if (passed) Icons.Filled.Check else Icons.Filled.Close

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = color, style = AppTheme.typography.body)
    }
}
