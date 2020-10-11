package me.oriharel.seriesmanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriesmanager.model.entity.Country
import me.oriharel.seriesmanager.model.entity.Language
import me.oriharel.seriesmanager.model.entity.company.ProductionCompany

@JsonIgnoreProperties(value = ["watched"])
class DetailedMovie @JsonCreator constructor(
        @JsonProperty("belongs_to_collection") val belongsToCollection: Any?,
        @JsonProperty("budget") val budget: Int,
        @JsonProperty("imdb_id") val imdbId: String?,
        @JsonProperty("production_countries") val productionCountries: List<Country>,
        @JsonProperty("revenue") val revenue: Int,
        @JsonProperty("tagline") val tagline: String?,
        @JsonProperty("adult") adult: Boolean,
        @JsonProperty("video") video: Boolean,
        @JsonProperty("backdrop_path") backdrop: String?,
        @JsonProperty("popularity") popularity: Double,
        @JsonProperty("original_title") originalName: String,
        @JsonProperty("genres") genres: List<Genre>,
        @JsonProperty("original_language") originalLanguage: String,
        @JsonProperty("poster_path") poster: String?,
        @JsonProperty("id") id: Int,
        @JsonProperty("vote_average") voteAverage: Double,
        @JsonProperty("overview") overview: String,
        @JsonProperty("release_date") releaseDate: Instant,
        @JsonProperty("vote_count") voteCount: Int,
        @JsonProperty("title") name: String,
        @JsonProperty("homepage") override val homepage: String,
        @JsonProperty("production_companies") override val productionCompanies: List<ProductionCompany>,
        @JsonProperty("runtime") val runtime: Int,
        @JsonProperty("spoken_languages") val languages: List<Language>,
        @JsonProperty("status") override val status: String,
        @JsonProperty("watched") watched: Boolean,
        @JsonProperty("lists") override var lists: Set<String>? = mutableSetOf(),
        override val mediaType: String = "movie"
) : Movie(
        adult,
        video,
        backdrop,
        popularity,
        originalName,
        genres.map { it.id },
        originalLanguage,
        poster,
        id,
        voteAverage,
        overview,
        releaseDate,
        voteCount,
        name,
        "movie",
        watched,
        1
), DetailedBroadcast {
    override fun toString(): String {
        return "DetailedMovie(belongsToCollection=$belongsToCollection, budget=$budget, imdbId=$imdbId, productionCountries=$productionCountries, revenue=$revenue, tagline=$tagline, homepage='$homepage', productionCompanies=$productionCompanies, runtime=$runtime, languages=$languages, status='$status')"
    }
}