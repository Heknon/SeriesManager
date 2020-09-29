package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriemanager.model.entity.person.CrewMember
import me.oriharel.seriemanager.model.entity.person.GuestStar

class Episode(
        @JsonProperty override val releaseDate: Instant,
        @JsonProperty val crew: List<CrewMember>,
        @JsonProperty val episodeNumber: Int,
        @JsonProperty val guestStars: List<GuestStar>,
        @JsonProperty override val name: String,
        @JsonProperty override val overview: String,
        @JsonProperty override val id: Int,
        @JsonProperty val productionCode: String,
        @JsonProperty val seasonNumber: Int,
        @JsonProperty override val poster: String?,
        @JsonProperty override val voteAverage: Double,
        @JsonProperty override val voteCount: Int
) : Broadcast(
        id,
        poster,
        overview,
        name,
        releaseDate,
        voteAverage,
        voteCount
)