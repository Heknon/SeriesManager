package me.oriharel.seriemanager.model.entity.person

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriemanager.model.entity.Person

data class CrewMember @JsonCreator constructor(
        @JsonProperty("id") override val id: Int,
        @JsonProperty("credit_id") override val creditId: String,
        @JsonProperty("name") override val name: String,
        @JsonProperty("department") val department: String,
        @JsonProperty("job") val job: String,
        @JsonProperty("gender") override val gender: Int,
        @JsonProperty("profile_path") override val profile: String?
) : Person(id, creditId, name, gender, profile)