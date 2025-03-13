package com.github.nullsafe.watchwise.core.data.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.github.nullsafe.watchwise.core.data.database.entity.*
import com.github.nullsafe.watchwise.core.data.database.entity.CreditsCast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

open class ListConverter<I>(private val gson: Gson) {
    @TypeConverter
    fun fromList(input: List<I>?): String? {
        return if (input == null) null
        else gson.toJson(input, object : TypeToken<List<I>>() {}.type)
    }

    @TypeConverter
    fun toList(json: String?): List<I> {
        return if (json == null) emptyList()
        else gson.fromJson(json, object : TypeToken<List<I>>() {}.type)
    }
}

@ProvidedTypeConverter
class ListIntConverter @Inject constructor(gson: Gson) : ListConverter<Int>(gson)

@ProvidedTypeConverter
class ListStringConverter @Inject constructor(gson: Gson) : ListConverter<String>(gson)

@ProvidedTypeConverter
class GenreConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun toList(value: String?): List<Genre>? {
        val listType = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Genre>?): String? {
        return if (list == null) null
        else gson.toJson(list, object : TypeToken<List<Genre>>() {}.type)
    }
}

@ProvidedTypeConverter
class ProductionCompanyConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun toList(value: String?): List<ProductionCompany>? {
        val listType = object : TypeToken<List<ProductionCompany>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<ProductionCompany>?): String? {
        return if (list == null) null
        else gson.toJson(list, object : TypeToken<List<ProductionCompany>>() {}.type)
    }
}

@ProvidedTypeConverter
class ProductionCountryConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun toList(value: String?): List<ProductionCountry>? {
        val listType = object : TypeToken<List<ProductionCountry>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<ProductionCountry>?): String? {
        return if (list == null) null
        else gson.toJson(list, object : TypeToken<List<ProductionCountry>>() {}.type)
    }
}

@ProvidedTypeConverter
class MovieCreditsCastConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun toList(value: String?): List<CreditsCast>? {
        val listType = object : TypeToken<List<CreditsCast>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<CreditsCast>?): String? {
        return if (list == null) null
        else gson.toJson(list, object : TypeToken<List<CreditsCast>>() {}.type)
    }
}

@ProvidedTypeConverter
class KnownForConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun toList(value: String?): List<KnownFor>? {
        val listType = object : TypeToken<List<KnownFor>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<KnownFor>?): String? {
        return if (list == null) null
        else gson.toJson(list, object : TypeToken<List<KnownFor>>() {}.type)
    }
}

@ProvidedTypeConverter
class MovieConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun fromList(value: List<Movie>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toList(value: String?): List<Movie>? {
        return value?.let {
            val type = object : TypeToken<List<Movie>>() {}.type
            gson.fromJson(it, type)
        }
    }
}

@ProvidedTypeConverter
class TvShowConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun toList(value: String?): List<TvShow>? {
        val listType = object : TypeToken<List<TvShow>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<TvShow>?): String? {
        return if (list == null) null
        else gson.toJson(list, object : TypeToken<List<TvShow>>() {}.type)
    }
}

@ProvidedTypeConverter
class CreatedByConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun toList(value: String?): List<CreatedBy>? {
        val listType = object : TypeToken<List<CreatedBy>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<CreatedBy>?): String? {
        return if (list == null) null
        else gson.toJson(list, object : TypeToken<List<CreatedBy>>() {}.type)
    }
}

@ProvidedTypeConverter
class NetworkConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun toList(value: String?): List<Network>? {
        val listType = object : TypeToken<List<Network>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Network>?): String? {
        return if (list == null) null
        else gson.toJson(list, object : TypeToken<List<Network>>() {}.type)
    }
}

@ProvidedTypeConverter
class SeasonConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun toList(value: String?): List<Season>? {
        val listType = object : TypeToken<List<Season>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Season>?): String? {
        return if (list == null) null
        else gson.toJson(list, object : TypeToken<List<Season>>() {}.type)
    }
}

@ProvidedTypeConverter
class PersonCreditsCastConverter @Inject constructor(private val gson: Gson) {
    @TypeConverter
    fun fromList(list: List<PersonCreditsCast>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(value: String?): List<PersonCreditsCast>? {
        return value?.let {
            val type = object : TypeToken<List<PersonCreditsCast>>() {}.type
            gson.fromJson(it, type)
        }
    }
}