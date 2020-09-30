package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ContentList @JsonCreator constructor(
        @JsonProperty("id") val id: UUID,
        @JsonProperty("name") val name: String,
        @JsonProperty("broadcasts") val broadcasts: List<Broadcast>
)