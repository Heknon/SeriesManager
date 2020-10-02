package me.oriharel.seriemanager.api.response

import com.fasterxml.jackson.annotation.JsonProperty

data class WatchtimeFormat(
        @JsonProperty("days") val days: Double,
        @JsonProperty("hours") val hours: Double,
        @JsonProperty("minutes") val minutes: Int,
        @JsonProperty("seconds") val seconds: Int,
) {
    companion object {
        fun build(minutes: Int): WatchtimeFormat {
            return WatchtimeFormat(minutes / 60.0 / 24.0, minutes / 60.0, minutes, minutes * 60)
        }
    }
}