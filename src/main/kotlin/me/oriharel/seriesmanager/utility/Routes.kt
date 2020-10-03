package me.oriharel.seriesmanager.utility

import me.oriharel.seriesmanager.dao.broadcast.SearchType

object Routes {
    object TMDB {
        const val API_KEY = "e721a557dd172dbdaf21acbec3976df2"
        const val BASE_URL = "https://api.themoviedb.org/3"
        const val ENDPOINT_END = "api_key=$API_KEY&language=en-US"
        const val IMAGES_API = "https://image.tmdb.org/t/p/w500"

        fun getPopularEndpoint(searchType: SearchType, page: Int): String {
            return "$BASE_URL/${searchType.name.toLowerCase()}/popular?page=$page&$ENDPOINT_END"
        }

        fun getTopEndpoint(searchType: SearchType, page: Int): String {
            return "$BASE_URL/${searchType.name.toLowerCase()}/top_rated?page=$page&$ENDPOINT_END"
        }

        fun getSimilarEndpoint(searchType: SearchType, broadcastId: Int, page: Int): String {
            return "$BASE_URL/${searchType.name.toLowerCase()}/$broadcastId/similar?page=$page&$ENDPOINT_END"
        }

        fun getRecommendationEndpoint(searchType: SearchType, broadcastId: Int, page: Int): String {
            return "$BASE_URL/${searchType.name.toLowerCase()}/$broadcastId/recommendations?page=$page&$ENDPOINT_END"
        }

        fun getTVShowEndpoint(id: Int): String {
            return "$BASE_URL/tv/${id}?$ENDPOINT_END"
        }

        fun getTVSeasonEndpoint(id: Int, season: Int): String {
            return "$BASE_URL/tv/${id}/season/${season}?$ENDPOINT_END"
        }

        fun getTVEpisodeEndpoint(id: Int, season: Int, episode: Int): String {
            return "$BASE_URL/tv/${id}/season/${season}/episode/${episode}?$ENDPOINT_END"
        }

        fun getMovieEndpoint(id: Int): String {
            return "$BASE_URL/movie/${id}?$ENDPOINT_END"
        }

        fun getSearchEndpoint(searchType: SearchType, query: String, page: Int, adult: Boolean): String {
            return "$BASE_URL/search/${searchType.name.toLowerCase()}?query=${query}&page=${page}&include_adult=${adult}&$ENDPOINT_END"
        }
    }
}