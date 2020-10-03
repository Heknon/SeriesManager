package me.oriharel.seriesmanager.service

import me.oriharel.seriesmanager.dao.broadcast.BroadcastDao
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.model.content.Episode
import me.oriharel.seriesmanager.model.content.Season
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
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
        return broadcastDao.episodesRemainingInShow(serializedBroadcast)
    }

    fun seasonEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast, season: Short): Int {
        return broadcastDao.episodesRemainingInSeason(serializedBroadcast, season)
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

    fun getBroadcastsWatchStatus(vararg serializedBroadcast: UserSerializedBroadcast): Map<String, MutableMap<Int, MutableList<Map<String, Boolean>>>> {
        return broadcastDao.getBroadcastsWatchStatus(*serializedBroadcast)
    }

    fun getDetailedSeason(serializedBroadcast: UserSerializedBroadcast, season: Int): Season {
        return broadcastDao.getDetailedSeason(serializedBroadcast, season)
    }

    fun getDetailedEpisode(serializedBroadcast: UserSerializedBroadcast, season: Int, episode: Int): Episode {
        return broadcastDao.getDetailedEpisode(serializedBroadcast, season, episode)
    }

    fun getMaxWatchtime(id: UUID, userService: UserService): Int {
        return broadcastDao.getMaxWatchtime(id, userService)
    }

    fun getMaxShowWatchtime(id: UUID, serializedBroadcast: UserSerializedBroadcast): Int {
        return broadcastDao.getMaxShowWatchtime(id, serializedBroadcast)
    }

    fun getMaxSeasonWatchtime(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Int): Int {
        return broadcastDao.getMaxSeasonWatchtime(id, serializedBroadcast, season)
    }

    fun getWatchtime(id: UUID, userService: UserService): Int {
        return broadcastDao.getWatchtime(id, userService)
    }

    fun getShowWatchtime(id: UUID, serializedBroadcast: UserSerializedBroadcast): Int {
        return broadcastDao.getShowWatchtime(id, serializedBroadcast)
    }

    fun getSeasonWatchtime(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Int): Int {
        return broadcastDao.getSeasonWatchtime(id, serializedBroadcast, season)
    }
}