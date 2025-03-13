package com.github.nullsafe.watchwise.core.data.mapper

import com.github.nullsafe.watchwise.core.data.database.entity.PersonDetail
import com.github.nullsafe.watchwise.core.data.dto.people.PersonDetailDto
import javax.inject.Inject

class PersonDetailMapper @Inject constructor() {
    fun map(dto: PersonDetailDto): PersonDetail {
        return PersonDetail(
            id = dto.id,
            name = dto.name,
            birthday = dto.birthday,
            knownForDepartment = dto.knownForDepartment,
            deathDay = dto.deathDay,
            gender = dto.gender,
            popularity = dto.popularity,
            biography = dto.biography,
            placeOfBirth = dto.placeOfBirth,
            profilePath = dto.profilePath,
            adult = dto.adult,
            homepage = dto.homepage,
            liked = false,  // Default value (can be updated in DB later)
            personCreditsCasts = emptyList() // Will be populated separately
        )
    }
}