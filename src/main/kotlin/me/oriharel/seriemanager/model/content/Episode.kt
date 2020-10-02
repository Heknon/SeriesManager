package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriemanager.model.entity.person.CrewMember
import me.oriharel.seriemanager.model.entity.person.GuestStar

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
        @JsonProperty("watched") override val watched: Boolean
) : Broadcast(
        id,
        poster,
        overview,
        name,
        voteAverage,
        voteCount,
        watched,
        releaseDate
)