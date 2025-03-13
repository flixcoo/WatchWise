package com.github.nullsafe.watchwise.liked.presentation.compose

data class TabComponentModel(
    val text: String,
    val selected: Boolean,
    val onClick: () -> Unit
)