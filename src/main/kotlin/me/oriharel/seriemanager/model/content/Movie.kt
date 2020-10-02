package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriemanager.Routes

@JsonIgnoreProperties(value = ["watched", "broadcast_count"])
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
        @JsonProperty("media_type", defaultValue = "movie") val type: String,
        @JsonProperty("watched") override var watched: Boolean,
        @JsonProperty("broadcast_count") override val broadcastCount: Int = 1
) : Broadcast(
        id,
        poster,
        overview,
        name,
        voteAverage,
        voteCount,
        watched,
        broadcastCount,
        releaseDate
) {
    val backdropUrl = "${Routes.IMAGES_API}$backdrop"
    override fun toString(): String {
        return "Movie(adult=$adult, video=$video, backdrop=$backdrop, popularity=$popularity, originalName=$originalName, genreIds=$genreIds, originalLanguage='$originalLanguage', poster=$poster, id=$id, voteAverage=$voteAverage, overview='$overview', releaseDate=$releaseDate, voteCount=$voteCount, name='$name', type='$type', watched=$watched, broadcastCount=$broadcastCount, backdropUrl='$backdropUrl')"
    }


}