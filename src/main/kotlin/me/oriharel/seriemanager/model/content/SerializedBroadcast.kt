package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class SerializedBroadcast @JsonCreator constructor(
        @NotBlank @JsonProperty("type") val type: String,
        @NotNull @Min(1) @JsonProperty("id") val id: Int?
) {
    constructor(tmdbId: Int, broadcast: SerializedBroadcast) : this(broadcast.type, tmdbId)
}