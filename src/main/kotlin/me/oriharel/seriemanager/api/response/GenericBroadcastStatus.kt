package me.oriharel.seriemanager.api.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class GenericBroadcastStatus @JsonCreator constructor(
        @JsonProperty("remaining_episodes_in_show") val remainingShowEpisodes: Int?,
        @JsonProperty("remaining_episodes_in_season") val remainingSeasonEpisodes: Int?,
        @JsonProperty("watched") val broadcastWatched: Boolean?

)