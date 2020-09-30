package me.oriharel.seriemanager.model.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @param id is iso_3166_1 code
 */
class Country @JsonCreator constructor(
        @JsonProperty("iso_3166_1") val id: String,
        @JsonProperty("name") val name: String
)