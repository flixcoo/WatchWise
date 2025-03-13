package com.github.nullsafe.watchwise.core.repository

import com.github.nullsafe.watchwise.core.data.database.entity.Genre
import com.github.nullsafe.watchwise.core.data.database.entity.GenreType
import kotlinx.coroutines.flow.Flow

interface GenresRepository {
    fun getGenres(
        genreType: GenreType,
        language: String?
    ): Flow<List<Genre>>
}