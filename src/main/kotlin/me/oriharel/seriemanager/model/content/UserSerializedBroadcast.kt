package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UserSerializedBroadcast @JsonCreator constructor(
        @NotBlank @JsonProperty("type") val type: String,
        @NotNull @Min(1) @JsonProperty("id") val id: Int?,
        @JsonProperty("watched") val watched: MutableMap<Short, MutableSet<Short>>? = mutableMapOf()
) {
    constructor(tmdbId: Int, broadcast: UserSerializedBroadcast) : this(broadcast.type, tmdbId, broadcast.watched)

    val totalEpisodesWatched: Int get() {
        var total = 0
        watched!!.forEach { pair ->
            pair.value.forEach {
                total += it
            }
        }
        return total
    }
}