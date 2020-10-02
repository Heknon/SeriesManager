package me.oriharel.seriemanager.api.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min

data class WatchStatus @JsonCreator constructor(
        @JsonProperty("watched") val watched: Boolean,
        @Min(0) @JsonProperty("season") val season: Short,
        @Min(0) @JsonProperty("episode") val episode: Short
)

data class WatchStatusBulk @JsonCreator constructor(
        @JsonProperty("watched") val watched: Boolean,
        @Min(0) @JsonProperty("season") val season: Short,
        @Min(0) @JsonProperty("episodes") val episodes: ShortArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WatchStatusBulk

        if (watched != other.watched) return false
        if (season != other.season) return false
        if (!episodes.contentEquals(other.episodes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = watched.hashCode()
        result = 31 * result + season
        result = 31 * result + episodes.contentHashCode()
        return result
    }
}