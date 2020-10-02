package me.oriharel.seriemanager.api.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min

data class WatchStatus @JsonCreator constructor(
        @JsonProperty("watched") val watched: Boolean,
        @Min(0) @JsonProperty("season") val season: Short,
        @Min(0) @JsonProperty("episode") val episode: Short
)