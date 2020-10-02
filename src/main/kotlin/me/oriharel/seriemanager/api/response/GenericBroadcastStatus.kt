package me.oriharel.seriemanager.api.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GenericBroadcastStatus @JsonCreator constructor(
        @JsonProperty("episode_name") var episodeName: String? = null,
        @JsonProperty("season_name") var seasonName: String? = null,
        @JsonProperty("broadcast_name") var name: String? = null,
        @JsonProperty("remaining_episodes_in_show") var remainingShowEpisodes: Int? = null,
        @JsonProperty("remaining_episodes_in_season") var remainingSeasonEpisodes: Int? = null,
        @JsonProperty("watched") var broadcastWatched: Boolean? = null,
)