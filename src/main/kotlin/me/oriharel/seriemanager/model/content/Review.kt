package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class Review @JsonCreator constructor(
        @JsonProperty("id") val id: String,
        @JsonProperty("author") val author: String,
        @JsonProperty("content") val content: String,
        @JsonProperty("iso_639_1") val iso_639_1: String,
        @JsonProperty("media_id") val mediaId: Int,
        @JsonProperty("media_title") val mediaTitle: String,
        @JsonProperty("media_type") val mediaType: String,
        @JsonProperty("url") val url: String
)