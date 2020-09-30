package me.oriharel.seriemanager.model.entity.person

import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriemanager.model.entity.Person

data class CastMember(
        @JsonProperty val character: String,
        @JsonProperty override val creditId: String,
        @JsonProperty override val gender: Int,
        @JsonProperty override val id: Int,
        @JsonProperty override val name: String,
        @JsonProperty val order: Int,
        @JsonProperty override val profile: String
) : Person(id, creditId, name, gender, profile) {
    constructor() : this("", "", -1, -1, "", -1, "")
}