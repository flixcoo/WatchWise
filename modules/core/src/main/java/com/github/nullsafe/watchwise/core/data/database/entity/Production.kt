package com.github.nullsafe.watchwise.core.data.database.entity

data class ProductionCompany(
    val id: Int,
    val name: String?,
    val originCountry: String?,
    val logoPath: String?
)

data class ProductionCountry(val iso: String?, val name: String?)