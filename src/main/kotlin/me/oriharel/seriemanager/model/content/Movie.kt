package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriemanager.Routes

@JsonIgnoreProperties("media_type")
open class Movie @JsonCreator constructor(
        @JsonProperty("adult") val adult: Boolean,
        @JsonProperty("video") val video: Boolean,
        @JsonProperty("backdrop_path") val backdrop: String?,
        @JsonProperty("popularity") val popularity: Double,
        @JsonProperty("original_title") val originalName: String?,
        @JsonProperty("genre_ids") val genreIds: List<Int>,
        @JsonProperty("original_language") val originalLanguage: String,
        @JsonProperty("poster_path") override val poster: String?,
        @JsonProperty("id") override val id: Int,
        @JsonProperty("vote_average") override val voteAverage: Double,
        @JsonProperty("overview") override val overview: String,
        @JsonProperty("release_date") override val releaseDate: Instant,
        @JsonProperty("vote_count") override val voteCount: Int,
        @JsonProperty("title") override val name: String,
        @JsonProperty("watched") override val watched: Boolean
) : Broadcast(
        id,
        poster,
        overview,
        name,
        voteAverage,
        voteCount,
        watched,
        releaseDate
) {
    val backdropUrl = "${Routes.IMAGES_API}$backdrop"
}