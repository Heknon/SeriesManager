package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class SerializedBroadcast(@NotBlank @JsonProperty val type: String, @NotNull @JsonProperty("id") val tmdbId: Int?) {
    constructor(tmdbId: Int, broadcast: SerializedBroadcast) : this(broadcast.type, tmdbId)
}