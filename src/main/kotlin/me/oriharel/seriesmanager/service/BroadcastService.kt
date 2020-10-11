package me.oriharel.seriesmanager.service

import me.oriharel.seriesmanager.dao.broadcast.BroadcastDao
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.model.content.Episode
import me.oriharel.seriesmanager.model.content.Season
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.security.CurrentUser
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

    fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): List<Broadcast> {
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

    fun getMaxWatchtime(id: UUID = CurrentUser.currentUserIdMustBeLoggedIn, userService: UserService): Int {
        return broadcastDao.getMaxWatchtime(id, userService)
    }

    fun getMaxShowWatchtime(id: UUID = CurrentUser.currentUserIdMustBeLoggedIn, broadcastId: Int, searchType: SearchType): Int {
        return broadcastDao.getMaxShowWatchtime(id, broadcastId, searchType)
    }

    fun getMaxSeasonWatchtime(id: UUID = CurrentUser.currentUserIdMustBeLoggedIn, broadcastId: Int, searchType: SearchType, season: Int): Int {
        return broadcastDao.getMaxSeasonWatchtime(id, broadcastId, searchType, season)
    }

    fun getWatchtime(id: UUID = CurrentUser.currentUserIdMustBeLoggedIn, userService: UserService): Int {
        return broadcastDao.getWatchtime(id, userService)
    }

    fun getShowWatchtime(id: UUID = CurrentUser.currentUserIdMustBeLoggedIn, broadcastId: Int, searchType: SearchType): Int {
        return broadcastDao.getShowWatchtime(id, broadcastId, searchType)
    }

    fun getSeasonWatchtime(id: UUID = CurrentUser.currentUserIdMustBeLoggedIn, broadcastId: Int, searchType: SearchType, season: Int): Int {
        return broadcastDao.getSeasonWatchtime(id, broadcastId, searchType, season)
    }

    fun getTopRated(serializedBroadcast: UserSerializedBroadcast, page: Int): List<Broadcast> {
        return broadcastDao.getTopRated(serializedBroadcast, page)
    }

    fun getPopular(serializedBroadcast: UserSerializedBroadcast, page: Int): List<Broadcast> {
        return broadcastDao.getPopular(serializedBroadcast, page)
    }

    fun getSimilar(serializedBroadcast: UserSerializedBroadcast, page: Int): List<Broadcast> {
        return broadcastDao.getSimilar(serializedBroadcast, page)
    }

    fun getPopular(searchType: SearchType, page: Int): List<Broadcast> {
        return broadcastDao.getPopular(searchType, page)
    }

    fun getTopRated(searchType: SearchType, page: Int): List<Broadcast> {
        return broadcastDao.getTopRated(searchType, page)
    }

    fun getRecommended(serializedBroadcast: UserSerializedBroadcast, page: Int): List<Broadcast> {
        return broadcastDao.getRecommended(serializedBroadcast, page)
    }
}