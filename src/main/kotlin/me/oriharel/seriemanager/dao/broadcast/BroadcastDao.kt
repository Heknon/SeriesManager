package me.oriharel.seriemanager.dao.broadcast

import me.oriharel.seriemanager.model.content.Broadcast
import me.oriharel.seriemanager.model.content.Episode
import me.oriharel.seriemanager.model.content.UserSerializedBroadcast
import java.util.*

interface BroadcastDao {
    fun getDetailedBroadcasts(vararg serializedBroadcast: UserSerializedBroadcast): List<Optional<Broadcast>>

    fun getDetailedBroadcast(serializedBroadcast: UserSerializedBroadcast): Optional<Broadcast>

    fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): List<Broadcast?>

    fun showEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast): Int

    fun seasonEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast, season: Short): Int

    fun seasonEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast, vararg season: Short): Map<Short, Int>

    fun episodeIsWatched(serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean

    fun movieIsWatched(vararg serializedBroadcast: UserSerializedBroadcast): Boolean

    fun getBroadcastsWatchStatus(vararg serializedBroadcast: UserSerializedBroadcast): Map<Broadcast, Map<Broadcast, Boolean>>
}