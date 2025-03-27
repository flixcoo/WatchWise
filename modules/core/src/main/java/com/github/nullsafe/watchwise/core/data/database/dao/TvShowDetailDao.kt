package com.github.nullsafe.watchwise.core.data.database.dao

import androidx.room.*
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDetailDao {

    @Query("SELECT * FROM TvShowDetail WHERE id = :id")
    suspend fun getById(id: Int): TvShowDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(tvShowDetail: TvShowDetail)

    @Delete
    suspend fun delete(tvShowDetail: TvShowDetail)

    @Transaction
    suspend fun toggleTvShowLike(tvShow: TvShowDetail) {
        val existing = getById(tvShow.id)
        if (existing == null) {
            save(tvShow.copy(liked = true))
        } else {
            save(existing.copy(liked = !existing.liked))
        }
    }

    @Transaction
    suspend fun toggleTvShowWatched(tvShow: TvShowDetail) {
        val existing = getById(tvShow.id)
        if (existing == null) {
            save(tvShow.copy(isWatched = true))
        } else {
            save(existing.copy(isWatched = !existing.isWatched, isUnwatched = false))
        }
    }

    @Transaction
    suspend fun toggleTvShowUnwatched(tvShow: TvShowDetail) {
        val existing = getById(tvShow.id)
        if (existing == null) {
            save(tvShow.copy(isUnwatched = true))
        } else {
            save(existing.copy(isUnwatched = !existing.isUnwatched, isWatched = false))
        }
    }

    @Query("SELECT EXISTS(SELECT * FROM TvShowDetail WHERE id = :id AND liked = 1)")
    suspend fun isTvShowSaved(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM TvShowDetail WHERE id = :id AND isWatched = 1)")
    suspend fun isTvShowWatched(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM TvShowDetail WHERE id = :id AND isUnwatched = 1)")
    suspend fun isTvShowUnwatched(id: Int): Boolean

    @Query("SELECT * FROM TvShowDetail WHERE liked = 1")
    fun getSavedTvShows(): Flow<List<TvShowDetail>>

    @Query("SELECT * FROM TvShowDetail WHERE isUnwatched = 1")
    fun getTvShowsUnwatched(): Flow<List<TvShowDetail>>

    @Query("SELECT * FROM TvShowDetail WHERE isWatched = 1")
    fun getTvShowsWatched(): Flow<List<TvShowDetail>>

    @Query("SELECT * FROM TvShowDetail WHERE liked = 1")
    fun getTvShowsFavourites(): Flow<List<TvShowDetail>>


}