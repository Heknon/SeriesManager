package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriemanager.model.entity.Language
import me.oriharel.seriemanager.model.entity.Person
import me.oriharel.seriemanager.model.entity.company.Network
import me.oriharel.seriemanager.model.entity.company.ProductionCompany

class DetailedTVShow(
        @JsonProperty val createdBy: Person,
        @JsonProperty override val runtime: Int,
        @JsonProperty override val homepage: String,
        @JsonProperty val inProduction: Boolean,
        @JsonProperty override val languages: List<Language>,
        @JsonProperty val lastAirDate: Instant,
        @JsonProperty val lastEpisodeToAir: Episode,
        @JsonProperty val nextEpisodeToAir: Episode?,
        @JsonProperty val networks: List<Network>,
        @JsonProperty val numberOfEpisodes: Int,
        @JsonProperty val numberOfSeasons: Int,
        @JsonProperty override val productionCompanies: List<ProductionCompany>,
        @JsonProperty val seasons: List<Season>,
        @JsonProperty override val status: String,
        @JsonProperty type: String,
        poster: String?,
        popularity: Double,
        id: Int,
        backdrop: String,
        voteAverage: Double,
        overview: String,
        releaseDate: Instant,
        originCountry: List<String>,
        genres: List<String>,
        originalLanguage: String,
        voteCount: Int,
        name: String,
        originalName: String
) : TVShow(
        poster,
        popularity,
        id,
        backdrop,
        voteAverage,
        overview,
        releaseDate,
        originCountry,
        genres,
        originalLanguage,
        voteCount,
        name,
        originalName
), DetailedBroadcast