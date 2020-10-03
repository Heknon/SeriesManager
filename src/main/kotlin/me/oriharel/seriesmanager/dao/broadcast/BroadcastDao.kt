package me.oriharel.seriesmanager.dao.broadcast

import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.model.content.Episode
import me.oriharel.seriesmanager.model.content.Season
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.service.UserService
import java.util.*

interface BroadcastDao {
    fun getDetailedBroadcasts(vararg serializedBroadcast: UserSerializedBroadcast): List<Optional<Broadcast>>

    fun getDetailedBroadcast(serializedBroadcast: UserSerializedBroadcast): Optional<Broadcast>

    fun getDetailedSeason(serializedBroadcast: UserSerializedBroadcast, season: Int): Season

    fun getDetailedEpisode(serializedBroadcast: UserSerializedBroadcast, season: Int, episode: Int): Episode

    fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): List<Broadcast?>

    fun episodesRemainingInShow(serializedBroadcast: UserSerializedBroadcast): Int

    fun episodesRemainingInSeason(serializedBroadcast: UserSerializedBroadcast, season: Short): Int

    fun seasonEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast, vararg season: Short): Map<Short, Int>

    fun episodeIsWatched(serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean

    fun movieIsWatched(vararg serializedBroadcast: UserSerializedBroadcast): Boolean

    fun getBroadcastsWatchStatus(vararg serializedBroadcast: UserSerializedBroadcast): Map<String, MutableMap<Int, MutableList<Map<String, Boolean>>>>

    fun getMaxWatchtime(id: UUID, userService: UserService): Int

    fun getMaxShowWatchtime(id: UUID, serializedBroadcast: UserSerializedBroadcast): Int

    fun getMaxSeasonWatchtime(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Int): Int

    fun getWatchtime(id: UUID, userService: UserService): Int

    fun getShowWatchtime(id: UUID, serializedBroadcast: UserSerializedBroadcast): Int

    fun getSeasonWatchtime(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Int): Int

}