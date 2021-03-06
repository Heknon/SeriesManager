package me.oriharel.seriesmanager.dao.broadcast

import me.oriharel.seriesmanager.model.content.*
import me.oriharel.seriesmanager.security.CurrentUser
import me.oriharel.seriesmanager.service.UserService
import me.oriharel.seriesmanager.utility.Mapper
import me.oriharel.seriesmanager.utility.Routes
import me.oriharel.seriesmanager.utility.convertURLJsonResponse
import me.oriharel.seriesmanager.utility.getJsonObject
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Repository("broadcastDao")
class BroadcastDataAccessService : BroadcastDao {
    override fun getDetailedBroadcasts(vararg serializedBroadcast: UserSerializedBroadcast): List<Optional<Broadcast>> {
        return serializedBroadcast.map { getDetailedBroadcast(it) }
    }

    override fun getDetailedBroadcast(serializedBroadcast: UserSerializedBroadcast): Optional<Broadcast> {
        return try {
            val broadcast: Broadcast = when (serializedBroadcast.type) {
                "movie" -> Routes.TMDB.getMovieEndpoint(serializedBroadcast.id
                        ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BROADCAST ID IS NULL - BROADCAST: $serializedBroadcast")).convertURLJsonResponse<DetailedMovie>()
                "tv" -> Routes.TMDB.getTVShowEndpoint(serializedBroadcast.id
                        ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BROADCAST ID IS NULL - BROADCAST: $serializedBroadcast")).convertURLJsonResponse<DetailedTVShow>()
                else -> return Optional.empty()
            }

            broadcast.watched = broadcast.broadcastCount <= serializedBroadcast.totalEpisodesWatched
            if (broadcast is DetailedBroadcast) broadcast.lists = serializedBroadcast.lists
            Optional.of(broadcast)
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }

    override fun getDetailedBroadcastMulti(broadcastId: Int, userService: UserService): Optional<List<Broadcast>> {
        return try {
            val broadcasts: MutableList<Broadcast> = mutableListOf()
            try {
                broadcasts.add(Routes.TMDB.getMovieEndpoint(broadcastId).convertURLJsonResponse<DetailedMovie>())
            } catch (e: Exception) {
                try {
                    broadcasts.add(Routes.TMDB.getTVShowEndpoint(broadcastId).convertURLJsonResponse<DetailedTVShow>())
                } catch (e: Exception) {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Broadcast ID. No broadcast exists with given ID.")
                }
            }

            if (CurrentUser.isLoggedIn)  {
                val serialized = userService.getBroadcastByIdMulti(broadcastId = broadcastId).get().associateBy { it.searchType }

                broadcasts.forEach {
                    if (it is DetailedBroadcast) {
                        it.watched = it.broadcastCount <= serialized[it.searchType]?.totalEpisodesWatched ?: 0
                        it.lists = serialized[it.searchType]?.lists
                    }
                }
            }
            Optional.of(broadcasts)
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }

    override fun getDetailedSeason(serializedBroadcast: UserSerializedBroadcast, season: Int): Season {
        return Routes.TMDB.getTVSeasonEndpoint(serializedBroadcast.id!!, season).convertURLJsonResponse()
    }

    override fun getDetailedEpisode(serializedBroadcast: UserSerializedBroadcast, season: Int, episode: Int): Episode {
        return Routes.TMDB.getTVEpisodeEndpoint(serializedBroadcast.id!!, season, episode).convertURLJsonResponse()
    }

    override fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): List<Broadcast> {
        val url = Routes.TMDB.getSearchEndpoint(searchType, query, page, adult)
        return getBroadcastsFromJSONList(url, searchType)
    }

    override fun episodesRemainingInShow(serializedBroadcast: UserSerializedBroadcast): Int {
        val bc = getDetailedBroadcast(serializedBroadcast).get()
        return bc.broadcastCount - serializedBroadcast.totalEpisodesWatched
    }

    override fun episodesRemainingInSeason(serializedBroadcast: UserSerializedBroadcast, season: Short): Int {
        val bc = getDetailedBroadcast(serializedBroadcast).get() as DetailedTVShow
        return bc.seasons[season.toInt() - 1].episodeCount - (serializedBroadcast.watched?.get(season)?.count() ?: 0)
    }

    override fun seasonEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast, vararg season: Short): Map<Short, Int> {
        val bc = getDetailedBroadcast(serializedBroadcast).get() as DetailedTVShow
        val map = mutableMapOf<Short, Int>()

        season.forEach {
            map[it] = bc.seasons[it.toInt() - 1].episodeCount - (serializedBroadcast.watched?.get(it)?.count() ?: 0)
        }

        return map
    }

    override fun episodeIsWatched(serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean {
        return serializedBroadcast.watched?.get(season)?.containsAll(episode.toList()) == true
    }

    override fun movieIsWatched(vararg serializedBroadcast: UserSerializedBroadcast): Boolean {
        serializedBroadcast.forEach {
            if (it.watched?.get(1)?.contains(1) == false) return false
        }

        return true
    }

    override fun getBroadcastsWatchStatus(vararg serializedBroadcast: UserSerializedBroadcast): Map<String, MutableMap<Int, MutableList<Map<String, Boolean>>>> {
        val broadcasts = getDetailedBroadcasts(*serializedBroadcast)
        val idSerializedMap = serializedBroadcast.associateBy { it.id }
        val result = mutableMapOf<String, MutableMap<Int, MutableList<Map<String, Boolean>>>>()

        broadcasts.forEach { broadcast ->
            val bc = broadcast.get()
            if (bc is DetailedMovie) {
                result[bc.name] = mutableMapOf(Pair(1, mutableListOf(mapOf(Pair(bc.name, bc.watched)))))
            } else if (bc is DetailedTVShow) {
                bc.seasons.forEach { seasonSmall ->
                    val season = getDetailedSeason(idSerializedMap[bc.id]!!, seasonSmall.seasonNumber)
                    season.episodes?.forEach {
                        val info = mapOf(Pair(it.name, episodeIsWatched(
                                idSerializedMap[bc.id]!!,
                                season.seasonNumber.toShort(),
                                it.episodeNumber.toShort()
                        )))
                        result[bc.name]?.get(season.seasonNumber)?.add(info)
                                ?: result[bc.name]?.set(season.seasonNumber, mutableListOf(info))
                                ?: result.set(bc.name, mutableMapOf(Pair(season.seasonNumber, mutableListOf(info))))
                    }
                }
            }
        }

        return result
    }

    override fun getMaxWatchtime(id: UUID, userService: UserService): Int {
        val broadcasts = getBroadcastsFromId(id, userService)
        var watchtime = 0
        broadcasts.forEach {
            if (it.isPresent) {
                val bc = it.get()
                if (bc is DetailedTVShow) {
                    watchtime += bc.runtime[0] * bc.numberOfEpisodes
                } else if (bc is DetailedMovie) {
                    watchtime += bc.runtime
                }
            }
        }

        return watchtime
    }

    override fun getMaxShowWatchtime(id: UUID, broadcastId: Int, searchType: SearchType): Int {
        val bc = getDetailedBroadcast(UserSerializedBroadcast(broadcastId, searchType)).get()

        if (bc is DetailedMovie) {
            return bc.runtime
        } else if (bc is DetailedTVShow) {
            return bc.runtime[0] * bc.numberOfEpisodes
        }
        return 0
    }

    override fun getMaxSeasonWatchtime(id: UUID, broadcastId: Int, searchType: SearchType, season: Int): Int {
        val bc = getDetailedBroadcast(UserSerializedBroadcast(broadcastId, searchType)).get()
        if (bc is DetailedMovie) {
            return bc.runtime
        } else if (bc is DetailedTVShow) {
            return bc.runtime[0] * bc.seasons[season - 1].episodeCount
        }
        return 0
    }

    override fun getWatchtime(id: UUID, userService: UserService): Int {
        val idToSerializedBroadcast = safelyGetSerializedBroadcasts(id, userService).associateBy { it.id!! }
        val broadcasts = getBroadcastsFromId(id, userService)
        var watchtime = 0

        broadcasts.forEach {
            val bc = it.get()
            if (bc is DetailedTVShow) {
                watchtime += bc.runtime[0] * (bc.numberOfEpisodes - episodesRemainingInShow(idToSerializedBroadcast[bc.id]!!))
            } else if (bc is DetailedMovie) {
                watchtime += bc.runtime
            }
        }

        return watchtime
    }

    override fun getShowWatchtime(id: UUID, broadcastId: Int, searchType: SearchType): Int {
        val bc = getDetailedBroadcast(UserSerializedBroadcast(broadcastId, searchType)).get()
        if (bc is DetailedMovie) {
            return bc.runtime
        } else if (bc is DetailedTVShow) {
            return bc.runtime[0] * (bc.numberOfEpisodes - episodesRemainingInShow(UserSerializedBroadcast(broadcastId, searchType)))
        }
        return 0
    }

    override fun getSeasonWatchtime(id: UUID, broadcastId: Int, searchType: SearchType, season: Int): Int {
        val bc = getDetailedBroadcast(UserSerializedBroadcast(broadcastId, searchType)).get()
        if (bc is DetailedMovie) {
            return bc.runtime
        } else if (bc is DetailedTVShow) {
            return bc.runtime[0] * (bc.seasons[season - 1].episodeCount - episodesRemainingInSeason(UserSerializedBroadcast(broadcastId, searchType), season.toShort()))
        }
        return 0
    }

    override fun getTopRated(searchType: SearchType, page: Int): List<Broadcast> {
        val url = Routes.TMDB.getTopEndpoint(searchType, page)
        return getBroadcastsFromJSONList(url, searchType)
    }

    override fun getPopular(searchType: SearchType, page: Int): List<Broadcast> {
        val url = Routes.TMDB.getPopularEndpoint(searchType, page)
        return getBroadcastsFromJSONList(url, searchType)
    }

    override fun getSimilar(serializedBroadcast: UserSerializedBroadcast, page: Int): List<Broadcast> {
        val searchType = if (serializedBroadcast.isMovie) SearchType.Movie else SearchType.Tv
        val url = Routes.TMDB.getSimilarEndpoint(searchType, serializedBroadcast.id!!, page)
        return getBroadcastsFromJSONList(url, searchType)
    }

    override fun getRecommended(serializedBroadcast: UserSerializedBroadcast, page: Int): List<Broadcast> {
        val searchType = if (serializedBroadcast.isMovie) SearchType.Movie else SearchType.Tv
        val url = Routes.TMDB.getRecommendationEndpoint(searchType, serializedBroadcast.id!!, page)
        return getBroadcastsFromJSONList(url, searchType)
    }

    private fun getBroadcastsFromJSONList(url: String, searchType: SearchType): List<Broadcast> {
        val jo = url.getJsonObject().getJSONArray("results")
        val results = mutableListOf<Broadcast?>()

        for (i in 0 until jo.length()) {
            val o = jo.getJSONObject(i)
            val mediaType = if (o.has("media_type")) o["media_type"] else searchType.name.toLowerCase()
            if (mediaType == "tv" || mediaType == "movie") {
                val oAsStr = o.toString(0)
                results.add(
                        when (mediaType) {
                            "movie" -> Mapper.mapper.readValue<Movie>(oAsStr, Movie::class.java)
                            "tv" -> Mapper.mapper.readValue<TVShow>(oAsStr, TVShow::class.java)
                            else -> null
                        }
                )
            }
        }

        return results.filterNotNull()
    }

    private fun getBroadcastsFromId(id: UUID, userService: UserService): List<Optional<Broadcast>> {
        return getDetailedBroadcasts(*safelyGetSerializedBroadcasts(id, userService).toTypedArray())
    }

    fun safelyGetSerializedBroadcasts(id: UUID, userService: UserService): Set<UserSerializedBroadcast> {
        val broadcastsOptional = userService.getBroadcasts(id)
        if (broadcastsOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")
        }
        return broadcastsOptional.get()
    }
}