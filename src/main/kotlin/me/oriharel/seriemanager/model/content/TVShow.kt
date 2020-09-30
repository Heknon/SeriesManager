package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriemanager.Routes

open class TVShow @JsonCreator constructor(
        @JsonProperty("poster_path") override val poster: String?,
        @JsonProperty("popularity") val popularity: Double,
        @JsonProperty("id") override val id: Int,
        @JsonProperty("backdrop_path") val backdrop: String?,
        @JsonProperty("vote_average") override val voteAverage: Double,
        @JsonProperty("overview") override val overview: String,
        @JsonProperty("first_air_date") override val releaseDate: Instant,
        @JsonProperty("origin_country") val originCountry: List<String>,
        @JsonProperty("genre_ids") val genreIds: List<Int>,
        @JsonProperty("original_language") val originalLanguage: String,
        @JsonProperty("vote_count") override val voteCount: Int,
        @JsonProperty("name") override val name: String,
        @JsonProperty("original_name") val originalName: String,
        @JsonProperty("media_type") open val type: String
) : Broadcast(
        id,
        poster,
        overview,
        name,
        voteAverage,
        voteCount,
        releaseDate
) {
    val backdropUrl = "${Routes.IMAGES_API}$backdrop"
}