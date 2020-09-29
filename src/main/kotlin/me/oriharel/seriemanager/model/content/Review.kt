package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty

class Review(
        @JsonProperty val id: String,
        @JsonProperty val author: String,
        @JsonProperty val content: String,
        @JsonProperty val iso_639_1: String,
        @JsonProperty val mediaId: Int,
        @JsonProperty val mediaTitle: String,
        @JsonProperty val mediaType: String,
        @JsonProperty val url: String
)