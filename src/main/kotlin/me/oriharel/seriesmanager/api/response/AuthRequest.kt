package me.oriharel.seriesmanager.api.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AuthRequest @JsonCreator constructor(
        @JsonProperty("username") val username: String,
        @JsonProperty("password") val password: String,
        @JsonProperty("stay_logged_in") val stayLoggedIn: Boolean?,
)