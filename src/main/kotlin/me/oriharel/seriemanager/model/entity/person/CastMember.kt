package me.oriharel.seriemanager.model.entity.person

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriemanager.model.entity.Person

data class CastMember @JsonCreator constructor(
        @JsonProperty("character") val character: String,
        @JsonProperty("credit_id") override val creditId: String,
        @JsonProperty("gender") override val gender: Int,
        @JsonProperty("id") override val id: Int,
        @JsonProperty("name") override val name: String,
        @JsonProperty("order") val order: Int,
        @JsonProperty("profile_path") override val profile: String?,
) : Person(id, creditId, name, gender, profile)