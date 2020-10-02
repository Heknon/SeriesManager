package me.oriharel.seriemanager.service

import me.oriharel.seriemanager.dao.broadcast.BroadcastDao
import me.oriharel.seriemanager.dao.broadcast.SearchType
import me.oriharel.seriemanager.model.content.Broadcast
import me.oriharel.seriemanager.model.content.UserSerializedBroadcast
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

@Service
class BroadcastService @Autowired constructor(@Qualifier("broadcastDao") private val broadcastDao: BroadcastDao) {
    fun getDetailedBroadcasts(vararg serializedBroadcast: UserSerializedBroadcast): List<Optional<Broadcast>> {
        return broadcastDao.getDetailedBroadcasts(*serializedBroadcast)
    }

    fun getDetailedBroadcast(serializedBroadcast: UserSerializedBroadcast): Optional<Broadcast> {
        return broadcastDao.getDetailedBroadcast(serializedBroadcast)
    }

    fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): List<Broadcast?> {
        return broadcastDao.findBroadcasts(searchType, query, page, adult)
    }

    fun showEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast): Int {
        return broadcastDao.showEpisodesRemaining(serializedBroadcast)
    }

    fun seasonEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast, season: Short): Int {
        return broadcastDao.seasonEpisodesRemaining(serializedBroadcast, season)
    }

    fun seasonEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast, vararg season: Short): Map<Short, Int> {
        return broadcastDao.seasonEpisodesRemaining(serializedBroadcast, *season)
    }

    fun episodeIsWatched(serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean {
        return broadcastDao.episodeIsWatched(serializedBroadcast, season, *episode)
    }

    fun movieIsWatched(vararg serializedBroadcast: UserSerializedBroadcast): Boolean {
        return broadcastDao.movieIsWatched(*serializedBroadcast)
    }

    fun getBroadcastsWatchStatus(vararg serializedBroadcast: UserSerializedBroadcast): Map<Broadcast, Map<Broadcast, Boolean>> {
        return broadcastDao.getBroadcastsWatchStatus(*serializedBroadcast)
    }
}