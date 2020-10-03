package me.oriharel.seriesmanager.model.entity.company

import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriesmanager.model.entity.Logo
import me.oriharel.seriesmanager.utility.Routes

data class Network(
        @JsonProperty("logo_path", required = false) override val logo: String?,
        @JsonProperty("logo", required = false) val logo_obj: Logo?,
        @JsonProperty("origin_country") override val originCountry: String,
        @JsonProperty("id") override val id: Int,
        @JsonProperty("name") override val name: String,
) : Company {
    val logoUrl = "${Routes.TMDB.IMAGES_API}/$logo"
}