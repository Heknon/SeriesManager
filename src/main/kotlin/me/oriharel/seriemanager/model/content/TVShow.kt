package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant

open class TVShow(
        @JsonProperty override val poster: String?,
        @JsonProperty val popularity: Double,
        @JsonProperty override val id: Int,
        @JsonProperty val backdrop: String,
        @JsonProperty override val voteAverage: Double,
        @JsonProperty override val overview: String,
        @JsonProperty override val releaseDate: Instant,
        @JsonProperty val originCountry: List<String>,
        @JsonProperty val genres: List<String>,
        @JsonProperty val originalLanguage: String,
        @JsonProperty override val voteCount: Int,
        @JsonProperty override val name: String,
        @JsonProperty val originalName: String
) : Broadcast(id, poster, overview, name, releaseDate, voteAverage, voteCount)