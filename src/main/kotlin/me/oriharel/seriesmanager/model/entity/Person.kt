package me.oriharel.seriesmanager.model.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriesmanager.utility.Routes

open class Person @JsonCreator constructor(
        @JsonProperty("id") override val id: Int,
        @JsonProperty("credit_id") open val creditId: String,
        @JsonProperty("name") override val name: String,
        @JsonProperty("gender") open val gender: Int,
        @JsonProperty("profile_path") open val profile: String?,
) : Entity {
    val profileUrl get() = "${Routes.TMDB.IMAGES_API}$profile"
}