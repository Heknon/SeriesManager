package me.oriharel.seriemanager.model.entity.company

import com.fasterxml.jackson.annotation.JsonProperty

class ProductionCompany(
        @JsonProperty override val logo: String,
        @JsonProperty override val originCountry: String,
        @JsonProperty override val id: Int,
        @JsonProperty override val name: String
) : Company