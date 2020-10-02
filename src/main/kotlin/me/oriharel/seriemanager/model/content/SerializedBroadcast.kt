package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import reactor.util.function.Tuple2
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class SerializedBroadcast @JsonCreator constructor(
        @NotBlank @JsonProperty("type") val type: String,
        @NotNull @Min(1) @JsonProperty("id") val id: Int?,
        @JsonProperty("watched") val watched: List<Tuple2<Short, Short>>
) {
    constructor(tmdbId: Int, broadcast: SerializedBroadcast) : this(broadcast.type, tmdbId, broadcast.watched)
}