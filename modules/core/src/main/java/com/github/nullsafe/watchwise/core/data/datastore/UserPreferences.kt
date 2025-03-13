package com.github.nullsafe.watchwise.core.data.datastore

data class UserPreferences(
    val userName: String = "",
    val userId: Int = 0,
    val language: String = "en",
    val isDarkMode: Boolean = false,
    val theme: String = ""
)