package com.github.nullsafe.watchwise.core.common.helper

object ImageProvider {
    private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"

    fun getImageUrl(path: String?): String {
        return if (!path.isNullOrEmpty()) "$BASE_IMAGE_URL$path" else ""
    }
}