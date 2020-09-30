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
    override fun <T> getDetailedBroadcasts(vararg serializedBroadcast: SerializedBroadcast): List<Optional<T>> {
        return serializedBroadcast.map { getDetailedBroadcast<T>(it) }
    }

    override fun <T> getDetailedBroadcast(serializedBroadcast: SerializedBroadcast): Optional<T> {
        return when (serializedBroadcast.type) {
            "movie" -> Optional.of(getMovieEndpoint(serializedBroadcast.id
                    ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BROADCAST ID IS NULL - BROADCAST: $serializedBroadcast")).convertURLJsonResponse<DetailedMovie>() as T)
            "tv" -> Optional.of(getTVShowEndpoint(serializedBroadcast.id
                    ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BROADCAST ID IS NULL - BROADCAST: $serializedBroadcast")).convertURLJsonResponse<DetailedTVShow>() as T)
            else -> Optional.empty()
        }
    }

    override fun <T : Broadcast> findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): List<T?> {
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

        return results as List<T?>
    }

    companion object {
        private fun getTVShowEndpoint(id: Int): String {
            return "${Routes.BASE_URL}/tv/${id}?api_key=${Routes.API_KEY}&language=en-US"
        }

        private fun getMovieEndpoint(id: Int): String {
            return "${Routes.BASE_URL}/movie/${id}?${Routes.ENDPOINT_END}"
        }

        private fun getSearchEndpoint(searchType: SearchType, query: String, page: Int, adult: Boolean): String {
            return "${Routes.BASE_URL}/search/${searchType.name.toLowerCase()}?query=${query}&page=${page}&include_adult=${adult}&${Routes.ENDPOINT_END}"
        }
    }
}