package com.github.nullsafe.watchwise.core.data.mapper

import com.github.nullsafe.watchwise.core.data.database.entity.CreditsCast
import com.github.nullsafe.watchwise.core.data.dto.credits.CreditsCastDto
import javax.inject.Inject

class CreditsCastMapper @Inject constructor() {
    fun map(dto: CreditsCastDto, movieId: Int?): CreditsCast =
        CreditsCast(
            id = dto.id,
            adult = dto.adult,
            gender = dto.gender,
            knownForDepartment = dto.knownForDepartment,
            name = dto.name,
            originalName = dto.originalName,
            popularity = dto.popularity,
            profilePath = dto.profilePath,
            castId = dto.castId,
            character = dto.character,
            creditId = dto.creditId,
            order = dto.order
        )
}
