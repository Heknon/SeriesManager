package me.oriharel.seriemanager.dao.broadcast

import me.oriharel.seriemanager.Routes
import me.oriharel.seriemanager.model.content.*
import me.oriharel.seriemanager.utility.Mapper
import me.oriharel.seriemanager.utility.convertURLJsonResponse
import me.oriharel.seriemanager.utility.getJsonObject
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
        val broadcast: Broadcast = when (serializedBroadcast.type) {
            "movie" -> getMovieEndpoint(serializedBroadcast.id
                    ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BROADCAST ID IS NULL - BROADCAST: $serializedBroadcast")).convertURLJsonResponse<DetailedMovie>()
            "tv" -> getTVShowEndpoint(serializedBroadcast.id
                    ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BROADCAST ID IS NULL - BROADCAST: $serializedBroadcast")).convertURLJsonResponse<DetailedTVShow>()
            else -> return Optional.empty()
        }

        broadcast.watched = broadcast.broadcastCount <= serializedBroadcast.totalEpisodesWatched
        return Optional.of(broadcast)
    }

    override fun getDetailedSeason(serializedBroadcast: UserSerializedBroadcast, season: Int): Season {
        return getTVSeasonEndpoint(serializedBroadcast.id!!, season).convertURLJsonResponse()
    }

    override fun getDetailedEpisode(serializedBroadcast: UserSerializedBroadcast, season: Int, episode: Int): Episode {
        return getTVEpisodeEndpoint(serializedBroadcast.id!!, season, episode).convertURLJsonResponse()
    }

    override fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): List<Broadcast?> {
        val url = getSearchEndpoint(searchType, query, page, adult)
        val jo = url.getJsonObject().getJSONArray("results")
        val results = mutableListOf<Broadcast?>()

        for (i in 0 until jo.length()) {
            val o = jo.getJSONObject(i)
            val mediaType = o["media_type"]
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

        return results
    }

    override fun showEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast): Int {
        val bc = getDetailedBroadcast(serializedBroadcast).get()
        return bc.broadcastCount - serializedBroadcast.totalEpisodesWatched
    }

    override fun seasonEpisodesRemaining(serializedBroadcast: UserSerializedBroadcast, season: Short): Int {
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

    companion object {
        private fun getTVShowEndpoint(id: Int): String {
            return "${Routes.BASE_URL}/tv/${id}?${Routes.ENDPOINT_END}"
        }

        private fun getTVSeasonEndpoint(id: Int, season: Int): String {
            return "${Routes.BASE_URL}/tv/${id}/season/${season}?${Routes.ENDPOINT_END}"
        }

        private fun getTVEpisodeEndpoint(id: Int, season: Int, episode: Int): String {
            return "${Routes.BASE_URL}/tv/${id}/season/${season}/episode/${episode}?${Routes.ENDPOINT_END}"
        }

        private fun getMovieEndpoint(id: Int): String {
            return "${Routes.BASE_URL}/movie/${id}?${Routes.ENDPOINT_END}"
        }

        private fun getSearchEndpoint(searchType: SearchType, query: String, page: Int, adult: Boolean): String {
            return "${Routes.BASE_URL}/search/${searchType.name.toLowerCase()}?query=${query}&page=${page}&include_adult=${adult}&${Routes.ENDPOINT_END}"
        }
    }
}