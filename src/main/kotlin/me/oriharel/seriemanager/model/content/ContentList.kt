package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ContentList(
        @JsonProperty val id: UUID,
        @JsonProperty val name: String,
        @JsonProperty val broadcasts: List<Broadcast>
)