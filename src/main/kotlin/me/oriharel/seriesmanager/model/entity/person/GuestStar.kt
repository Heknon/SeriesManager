package me.oriharel.seriesmanager.model.entity.person

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriesmanager.model.entity.Person

data class GuestStar @JsonCreator constructor(
        @JsonProperty("id") override val id: Int,
        @JsonProperty("name") override val name: String,
        @JsonProperty("credit_id") override val creditId: String,
        @JsonProperty("character") val character: String,
        @JsonProperty("order") val order: Int,
        @JsonProperty("gender") override val gender: Int,
        @JsonProperty("profile_path") override val profile: String?,
) : Person(id, creditId, name, gender, profile)