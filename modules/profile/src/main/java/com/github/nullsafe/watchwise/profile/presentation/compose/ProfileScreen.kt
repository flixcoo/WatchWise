package com.github.nullsafe.watchwise.profile.presentation.compose

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    Surface {
        Text("Profile Screen")
    }
}