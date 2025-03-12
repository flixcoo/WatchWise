package com.github.nullsafe.watchwise.core.data.mapper

import com.github.nullsafe.watchwise.core.data.database.entity.Video
import com.github.nullsafe.watchwise.core.data.dto.movie.VideoDto
import javax.inject.Inject

class VideoMapper @Inject constructor() {
    fun map(dto: VideoDto): Video {
        return Video(
            id = dto.videoId,
            name = dto.name,
            key = dto.key,
            site = dto.site,
            type = dto.type,
            publishedAt = dto.publishedAt,
            isOfficial = dto.official
        )
    }
}
