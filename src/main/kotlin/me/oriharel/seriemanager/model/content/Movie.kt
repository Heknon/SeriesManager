package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant

open class Movie(
        @JsonProperty val adult: Boolean,
        @JsonProperty val video: Boolean,
        @JsonProperty val backdrop: String,
        @JsonProperty val popularity: Double,
        @JsonProperty val originalName: String,
        @JsonProperty val originCountry: List<String>,
        @JsonProperty val genres: List<String>,
        @JsonProperty val originalLanguage: String,
        @JsonProperty override val poster: String?,
        @JsonProperty override val id: Int,
        @JsonProperty override val voteAverage: Double,
        @JsonProperty override val overview: String,
        @JsonProperty override val releaseDate: Instant,
        @JsonProperty override val voteCount: Int,
        @JsonProperty override val name: String
) : Broadcast(
        id,
        poster,
        overview,
        name,
        releaseDate,
        voteAverage,
        voteCount
)