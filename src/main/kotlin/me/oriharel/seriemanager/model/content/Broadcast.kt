package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriemanager.Routes
import org.springframework.data.annotation.Id

abstract class Broadcast(
        @Id @JsonProperty("id") open val id: Int,
        @JsonProperty("poster_path") open val poster: String?,
        @JsonProperty("overview") open val overview: String,
        @JsonProperty("name") open val name: String,
        @JsonProperty("vote_average") open val voteAverage: Double,
        @JsonProperty("vote_count") open val voteCount: Int,
        open val releaseDate: Instant
) {
    val posterUrl = "${Routes.IMAGES_API}$poster"
}