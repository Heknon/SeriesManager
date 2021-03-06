package me.oriharel.seriesmanager.model.entity.company

import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriesmanager.utility.Routes

class ProductionCompany(
        @JsonProperty("logo_path") override val logo: String?,
        @JsonProperty("origin_country") override val originCountry: String,
        @JsonProperty("id") override val id: Int,
        @JsonProperty("name") override val name: String,
) : Company {
    val logoUrl = "${Routes.TMDB.IMAGES_API}/$logo"
}