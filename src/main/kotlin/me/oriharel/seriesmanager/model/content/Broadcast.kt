package me.oriharel.seriesmanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriesmanager.utility.Routes
import org.springframework.data.annotation.Id

/**
 * @property watched only works with DetailedTVShow, Movie and Season
 */
abstract class Broadcast(
        @Id @JsonProperty("id") open val id: Int,
        @JsonProperty("poster_path") open val poster: String?,
        @JsonProperty("overview") open val overview: String,
        @JsonProperty("name") open val name: String,
        @JsonProperty("vote_average") open val voteAverage: Double,
        @JsonProperty("vote_count") open val voteCount: Int,
        @JsonProperty("watched") open var watched: Boolean,
        @JsonProperty("broadcast_count") open val broadcastCount: Int,
        open val releaseDate: Instant?,
) {
    val posterUrl get() = "${Routes.TMDB.IMAGES_API}$poster"
}