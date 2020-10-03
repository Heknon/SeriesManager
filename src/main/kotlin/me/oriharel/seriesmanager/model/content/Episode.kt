package me.oriharel.seriesmanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriesmanager.model.entity.person.CrewMember
import me.oriharel.seriesmanager.model.entity.person.GuestStar

@JsonIgnoreProperties(value = ["watched", "broadcastCount"])
class Episode @JsonCreator constructor(
        @JsonProperty("air_date") override val releaseDate: Instant,
        @JsonProperty("crew") val crew: List<CrewMember>?,
        @JsonProperty("episode_number") val episodeNumber: Int,
        @JsonProperty("guest_stars") val guestStars: List<GuestStar>?,
        @JsonProperty("name") override val name: String,
        @JsonProperty("overview") override val overview: String,
        @JsonProperty("id") override val id: Int,
        @JsonProperty("show_id") val showId: Int,
        @JsonProperty("production_code") val productionCode: String,
        @JsonProperty("season_number") val seasonNumber: Int,
        @JsonProperty("still_path") override val poster: String?,
        @JsonProperty("vote_average") override val voteAverage: Double,
        @JsonProperty("vote_count") override val voteCount: Int,
        @JsonProperty("watched") override var watched: Boolean,
) : Broadcast(
        id,
        poster,
        overview,
        name,
        voteAverage,
        voteCount,
        watched,
        1,
        releaseDate
) {
    override fun toString(): String {
        return "Episode(releaseDate=$releaseDate, crew=$crew, episodeNumber=$episodeNumber, guestStars=$guestStars, name='$name', overview='$overview', id=$id, showId=$showId, productionCode='$productionCode', seasonNumber=$seasonNumber, poster=$poster, voteAverage=$voteAverage, voteCount=$voteCount, watched=$watched, broadcastCount=$broadcastCount)"
    }
}