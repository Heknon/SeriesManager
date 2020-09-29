package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant

class Season(
        @JsonProperty val seasonNumber: Int,
        @JsonProperty val episodes: List<Episode>,
        @JsonProperty override val id: Int,
        @JsonProperty override val poster: String?,
        @JsonProperty override val overview: String,
        @JsonProperty override val name: String,
        @JsonProperty override val releaseDate: Instant
) : Broadcast(
        id,
        poster,
        overview,
        name,
        releaseDate,
        -1.0,
        -1
)