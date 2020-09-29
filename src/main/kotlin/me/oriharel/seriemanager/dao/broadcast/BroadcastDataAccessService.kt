package me.oriharel.seriemanager.dao.broadcast

import com.beust.klaxon.JsonObject
import me.oriharel.seriemanager.model.content.Broadcast
import me.oriharel.seriemanager.model.content.SerializedBroadcast
import me.oriharel.seriemanager.utility.convertURLJsonResponse
import org.json.JSONObject
import org.springframework.stereotype.Repository
import java.util.*

@Repository("broadcastDao")
class BroadcastDataAccessService : BroadcastDao {
    override fun getDetailedBroadcasts(vararg serializedBroadcast: SerializedBroadcast): List<Optional<JSONObject>> {
        return serializedBroadcast.map { getDetailedBroadcast(it) }
    }

    override fun getDetailedBroadcast(serializedBroadcast: SerializedBroadcast): Optional<JSONObject> {
        return when (serializedBroadcast.type) {
            "movie" -> Optional.of(getMovieEndpoint(serializedBroadcast.tmdbId!!).convertURLJsonResponse())
            "tv" -> Optional.of(getTVShowEndpoint(serializedBroadcast.tmdbId!!).convertURLJsonResponse())
            else -> Optional.empty()
        }
    }

    override fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): JSONObject {
        return getSearchEndpoint(searchType, query, page, adult).convertURLJsonResponse()
    }

    companion object {
        private const val API_KEY = "e721a557dd172dbdaf21acbec3976df2"
        private const val BASE_URL = "https://api.themoviedb.org/3"
        private const val ENDPOINT_END = "api_key=$API_KEY&language=en-US"

        private fun getTVShowEndpoint(id: Int): String {
            return "$BASE_URL/tv/${id}?api_key=$API_KEY&language=en-US"
        }

        private fun getMovieEndpoint(id: Int): String {
            return "$BASE_URL/movie/${id}?$ENDPOINT_END"
        }

        private fun getSearchEndpoint(searchType: SearchType, query: String, page: Int, adult: Boolean): String {
            return "$BASE_URL/search/${searchType.name}?query=${query}&page=${page}&include_adult=${adult}&$ENDPOINT_END"
        }
    }
}