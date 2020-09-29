package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import org.springframework.data.annotation.Id


abstract class Broadcast(
        @Id @JsonProperty open val id: Int,
        @JsonProperty open val poster: String?,
        @JsonProperty open val overview: String,
        @JsonProperty open val name: String,
        @JsonProperty open val releaseDate: Instant,
        @JsonProperty open val voteAverage: Double,
        @JsonProperty open val voteCount: Int)