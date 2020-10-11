package me.oriharel.seriesmanager.api.broadcast

import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.security.CurrentUser
import me.oriharel.seriesmanager.service.BroadcastService
import me.oriharel.seriesmanager.service.UserService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

abstract class UserRecommendationsController constructor(
        private val userService: UserService,
        private val broadcastService: BroadcastService,
        private val searchType: SearchType,
) {
    protected open fun getRecommendations(
            similar: Boolean? = false,
            similarPage: Int? = 1,
            recommendations: Boolean? = false,
            recommendationsPage: Int? = 1,
            top: Boolean? = false,
            topPage: Int? = 1,
            popular: Boolean? = false,
            popularPage: Int? = 1,
    ): MutableMap<Int, MutableMap<String, List<Broadcast>>> {
        val serializedBroadcasts = userService.getBroadcasts(CurrentUser.currentUserIdMustBeLoggedIn).get()
        val recommendationResults: MutableMap<Int, MutableMap<String, List<Broadcast>>> = mutableMapOf()

        serializedBroadcasts.forEach {
            recommendationResults[it.id!!] = getRecommendationsBySerialized(
                    it,
                    top,
                    similar,
                    recommendations,
                    popular,
                    similarPage,
                    recommendationsPage,
                    topPage,
                    popularPage
            )
        }

        return recommendationResults
    }

    protected open fun getRecommendationsById(
            broadcastId: Int,
            similar: Boolean? = false,
            similarPage: Int? = 1,
            recommendations: Boolean? = false,
            recommendationsPage: Int? = 1,
            top: Boolean? = false,
            topPage: Int? = 1,
            popular: Boolean? = false,
            popularPage: Int? = 1,
    ): Map<String, List<Broadcast>> {
        val serializedBroadcast = UserSerializedBroadcast(broadcastId, searchType)
        return getRecommendationsBySerialized(
                serializedBroadcast,
                top,
                similar,
                recommendations,
                popular,
                similarPage,
                recommendationsPage,
                topPage,
                popularPage
        )
    }

    private fun getRecommendationsBySerialized(
            serializedBroadcast: UserSerializedBroadcast,
            top: Boolean?,
            similar: Boolean?,
            recommendations: Boolean?,
            popular: Boolean?,
            similarPage: Int?,
            recommendationsPage: Int?,
            topPage: Int?,
            popularPage: Int?,
    ): MutableMap<String, List<Broadcast>> {
        val recommendationResult: MutableMap<String, List<Broadcast>> = mutableMapOf()

        if (similar == true) recommendationResult["similar"] = broadcastService.getSimilar(serializedBroadcast, similarPage!!)
        if (recommendations == true) recommendationResult["recommendations"] = broadcastService.getRecommended(serializedBroadcast, recommendationsPage!!)
        if (top == true) recommendationResult["top"] = broadcastService.getTopRated(serializedBroadcast, topPage!!)
        if (popular == true) recommendationResult["popular"] = broadcastService.getPopular(serializedBroadcast, popularPage!!)

        return recommendationResult
    }

}