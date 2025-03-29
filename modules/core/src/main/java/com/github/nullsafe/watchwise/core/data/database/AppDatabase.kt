package com.github.nullsafe.watchwise.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.nullsafe.watchwise.core.data.database.converter.*
import com.github.nullsafe.watchwise.core.data.database.dao.*
import com.github.nullsafe.watchwise.core.data.database.entity.*

@Database(
    entities = [
        Movie::class,
        TvShow::class,
        TvShowDetail::class,
        MovieDetail::class,
        PersonDetail::class,
        Person::class,
        Genre::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    value = [
        ListIntConverter::class,
        ListStringConverter::class,
        GenreConverter::class,
        ProductionCompanyConverter::class,
        ProductionCountryConverter::class,
        MovieCreditsCastConverter::class,
        KnownForConverter::class,
        MovieConverter::class,
        TvShowConverter::class,
        CreatedByConverter::class,
        NetworkConverter::class,
        SeasonConverter::class,
        PersonCreditsCastConverter::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun movieDetailDao(): MovieDetailDao
    abstract fun personDao(): PersonDao
    abstract fun personDetailDao(): PersonDetailDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun tvShowDetailDao(): TvShowDetailDao
    abstract fun genresDao(): GenresDao

    /*companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE MovieDetail ADD COLUMN username TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE TvShowDetail ADD COLUMN username TEXT NOT NULL DEFAULT ''")
            }
        }
    }*/
}