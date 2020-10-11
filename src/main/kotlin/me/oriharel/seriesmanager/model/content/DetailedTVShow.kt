package me.oriharel.seriesmanager.model.content

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriesmanager.model.entity.Person
import me.oriharel.seriesmanager.model.entity.company.Network
import me.oriharel.seriesmanager.model.entity.company.ProductionCompany

@JsonIgnoreProperties(value = ["watched", "broadcastCount", "mediaType", "lists"])
class DetailedTVShow @JsonCreator constructor(
        @JsonProperty("created_by") val createdBy: List<Person>,
        @JsonProperty("episode_run_time") val runtime: List<Int>,
        @JsonProperty("homepage") override val homepage: String,
        @JsonProperty("in_production") val inProduction: Boolean,
        @JsonProperty("languages") val languages: List<String>,
        @JsonProperty("last_air_date") val lastAirDate: Instant?,
        @JsonProperty("last_episode_to_air") val lastEpisodeToAir: Episode?,
        @JsonProperty("next_episode_to_air") val nextEpisodeToAir: Episode?,
        @JsonProperty("number_of_episodes") val numberOfEpisodes: Int,
        @JsonProperty("number_of_seasons") val numberOfSeasons: Int,
        @JsonProperty("production_companies") override val productionCompanies: List<ProductionCompany>,
        @JsonProperty("seasons") val seasons: List<Season>,
        @JsonProperty("status") override val status: String,
        @JsonProperty("type") override val type: String,
        @JsonProperty("poster_path") poster: String?,
        @JsonProperty("popularity") popularity: Double,
        @JsonProperty("networks") networks: List<Network>,
        @JsonProperty("id") id: Int,
        @JsonProperty("backdrop_path") backdrop: String?,
        @JsonProperty("vote_average") voteAverage: Double,
        @JsonProperty("overview") overview: String,
        @JsonProperty("first_air_date") releaseDate: Instant?,
        @JsonProperty("origin_country") originCountry: List<String>,
        @JsonProperty("genres") genres: List<Genre>,
        @JsonProperty("original_language") originalLanguage: String,
        @JsonProperty("vote_count") voteCount: Int,
        @JsonProperty("name") name: String,
        @JsonProperty("original_name") originalName: String,
        @JsonProperty("watched") watched: Boolean,
        @JsonProperty("lists") override var lists: Set<String>?,
        @JsonProperty("mediaType") override val mediaType: String? = "tv"
) : TVShow(
        poster,
        popularity,
        id,
        backdrop,
        voteAverage,
        overview,
        releaseDate,
        originCountry,
        genres.map { it.id },
        originalLanguage,
        voteCount,
        name,
        originalName,
        type,
        watched,
        numberOfEpisodes,
        networks

), DetailedBroadcast {
    override fun toString(): String {
        return "DetailedTVShow(createdBy=$createdBy, runtime=$runtime, homepage='$homepage', inProduction=$inProduction, languages=$languages, lastAirDate=$lastAirDate, lastEpisodeToAir=$lastEpisodeToAir, nextEpisodeToAir=$nextEpisodeToAir, networks=$networks, numberOfEpisodes=$numberOfEpisodes, numberOfSeasons=$numberOfSeasons, productionCompanies=$productionCompanies, seasons=$seasons, status='$status', type='$type', broadcastCount=$broadcastCount)"
    }
}