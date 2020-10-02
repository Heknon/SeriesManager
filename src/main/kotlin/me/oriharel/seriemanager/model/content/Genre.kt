package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Genre @JsonCreator constructor(
        @JsonProperty("id") val id: Int,
        @JsonProperty("name") val name: String,
)