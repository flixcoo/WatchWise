package com.github.nullsafe.watchwise.core.data.database.entity

data class Video(
    val id: String,
    val name: String,
    val key: String,
    val site: String,
    val type: String,
    val publishedAt: String,
    val isOfficial: Boolean
)