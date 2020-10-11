package me.oriharel.seriesmanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


data class UserSerializedBroadcast @JsonCreator constructor(
        @NotBlank @JsonProperty("type") val type: String,
        @NotNull @Min(1) @JsonProperty("id") val id: Int?,
        @JsonProperty("watched") val watched: MutableMap<Short, MutableSet<Short>>? = mutableMapOf(),
        @JsonProperty("lists") val lists: MutableSet<String> = mutableSetOf(),
) {
    constructor(tmdbId: Int, broadcast: UserSerializedBroadcast) : this(broadcast.type, tmdbId, broadcast.watched, broadcast.lists)
    constructor(id: Int, searchType: SearchType, safe: Boolean = true) : this(searchType.toString().toLowerCase(), id, mutableMapOf(), mutableSetOf()) {
        if (safe && searchType == SearchType.Multi) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Type must be 'movie' or 'tv'")
        }
    }

    val totalEpisodesWatched: Int
        get() {
            var total = 0
            watched!!.forEach { pair ->
                total += pair.value.count()
            }
            return total
        }

    val searchType get() = if (isMovie) SearchType.Movie else SearchType.Tv

    val isMovie get() = type.equals("movie", ignoreCase = true)

    val isTVShow get() = type.equals("tv", ignoreCase = true)
}