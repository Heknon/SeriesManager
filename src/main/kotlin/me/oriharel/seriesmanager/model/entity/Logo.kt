package me.oriharel.seriesmanager.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriesmanager.utility.Routes

data class Logo(
        @JsonProperty("path") val path: String,
        @JsonProperty("aspect_ratio") val aspectRatio: Double,
) {
    val profileUrl get() = "${Routes.TMDB.IMAGES_API}$path"
}