package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant

@JsonIgnoreProperties(value = ["watched", "broadcast_count", "_id"])
class Season @JsonCreator constructor(
        @JsonProperty("season_number") val seasonNumber: Int,
        @JsonProperty("episode_count") val episodeCount: Int,
        @JsonProperty("episodes") val episodes: List<Episode>?,
        @JsonProperty("id") override val id: Int,
        @JsonProperty("poster_path") override val poster: String?,
        @JsonProperty("overview") override val overview: String,
        @JsonProperty("name") override val name: String,
        @JsonProperty("air_date") override val releaseDate: Instant?,
        @JsonProperty("watched") override var watched: Boolean
) : Broadcast(
        id,
        poster,
        overview,
        name,
        -1.0,
        -1,
        watched,
        episodeCount,
        releaseDate
) {
    override fun toString(): String {
        return "Season(seasonNumber=$seasonNumber, episodeCount=$episodeCount, episodes=$episodes, id=$id, poster=$poster, overview='$overview', name='$name', releaseDate=$releaseDate, watched=$watched, broadcastCount=$broadcastCount)"
    }
}