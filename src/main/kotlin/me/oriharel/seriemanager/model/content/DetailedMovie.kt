package me.oriharel.seriemanager.model.content

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.Instant
import me.oriharel.seriemanager.model.entity.Country
import me.oriharel.seriemanager.model.entity.Language
import me.oriharel.seriemanager.model.entity.company.ProductionCompany

class DetailedMovie(
        @JsonProperty val belongsToCollection: Any?,
        @JsonProperty val budget: Int,
        @JsonProperty val imdbId: String?,
        @JsonProperty val productionCountries: List<Country>,
        @JsonProperty val revenue: Int,
        @JsonProperty val tagline: String?,
        adult: Boolean,
        video: Boolean,
        backdrop: String,
        popularity: Double,
        originalName: String,
        originCountry: List<String>,
        genres: List<String>,
        originalLanguage: String,
        poster: String?,
        id: Int,
        voteAverage: Double,
        overview: String,
        releaseDate: Instant,
        voteCount: Int,
        name: String,
        @JsonProperty override val homepage: String,
        @JsonProperty override val productionCompanies: List<ProductionCompany>,
        @JsonProperty override val runtime: Int,
        @JsonProperty override val languages: List<Language>,
        @JsonProperty override val status: String
) : Movie(
        adult,
        video,
        backdrop,
        popularity,
        originalName,
        originCountry,
        genres,
        originalLanguage,
        poster,
        id,
        voteAverage,
        overview,
        releaseDate,
        voteCount,
        name
), DetailedBroadcast