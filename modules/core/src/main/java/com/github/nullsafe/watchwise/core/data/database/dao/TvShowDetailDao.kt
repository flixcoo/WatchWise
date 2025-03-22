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
            delete(tvShow)
        }
    }

    @Query("SELECT EXISTS(SELECT * FROM TvShowDetail WHERE id = :id)")
    suspend fun isTvShowSaved(id: Int): Boolean

    @Query("SELECT * FROM TvShowDetail WHERE liked = 1")
    fun getSavedTvShows(): Flow<List<TvShowDetail>>

    // Neue Abfragen für die Kategorien
    @Query("SELECT * FROM TvShowDetail WHERE isWatched = 0 AND liked = 0")
    fun getTvShowsToWatch(): Flow<List<TvShowDetail>>

    @Query("SELECT * FROM TvShowDetail WHERE isWatched = 1")
    fun getTvShowsSeen(): Flow<List<TvShowDetail>>

    @Query("SELECT * FROM TvShowDetail WHERE isFavourite = 1")
    fun getTvShowsFavourites(): Flow<List<TvShowDetail>>

    // Methode zum Markieren einer Serie als "gesehen"
    @Update
    suspend fun markTvShowAsWatched(tvShow: TvShowDetail)

    // Methode zum Markieren einer Serie als "Favorit"
    @Update
    suspend fun markTvShowAsFavourite(tvShow: TvShowDetail)
}