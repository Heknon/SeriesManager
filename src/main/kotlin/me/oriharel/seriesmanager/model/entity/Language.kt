package me.oriharel.seriesmanager.model.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @param id is iso_639_1 code
 */
class Language @JsonCreator constructor(
        @JsonProperty("iso_639_1") val id: String,
        @JsonProperty("name") val name: String,
)