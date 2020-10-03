package me.oriharel.seriesmanager.api.user

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.service.BroadcastService
import me.oriharel.seriesmanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/v1/user/{userId}/recommend", produces = ["application/json"], consumes = ["application/json"])
class UserRecommendationsController @Autowired constructor(
        private val userService: UserService,
        private val broadcastService: BroadcastService,
) {
    @Operation(summary = "Get similar, recommended, top, and popular broadcasts for each broadcast user is following")
    @GetMapping
    fun getRecommendations(
            @PathVariable("userId") userId: UUID,
            @RequestParam(defaultValue = "false") similar: Boolean? = false,
            @RequestParam(value = "similar_page", defaultValue = "1") similarPage: Int? = 1,
            @RequestParam(defaultValue = "false") recommendations: Boolean? = false,
            @RequestParam(value = "recommendations_page", defaultValue = "1") recommendationsPage: Int? = 1,
            @RequestParam(defaultValue = "false") top: Boolean? = false,
            @RequestParam(value = "top_page", defaultValue = "1") topPage: Int? = 1,
            @RequestParam(defaultValue = "false") popular: Boolean? = false,
            @RequestParam(value = "popular_page", defaultValue = "1") popularPage: Int? = 1,
    ): MutableMap<Int, MutableMap<String, List<Broadcast>>> {
        val serializedBroadcasts = userService.getBroadcasts(userId).get()
        val recommendationResults: MutableMap<Int, MutableMap<String, List<Broadcast>>> = mutableMapOf()

        serializedBroadcasts.forEach {
            recommendationResults[it.id!!] = mutableMapOf()
            if (similar == true) recommendationResults[it.id]?.set("similar", broadcastService.getSimilar(it, similarPage!!))
            if (recommendations == true) recommendationResults[it.id]?.set("recommendations", broadcastService.getRecommended(it, recommendationsPage!!))
            if (top == true) recommendationResults[it.id]?.set("top", broadcastService.getTopRated(it, topPage!!))
            if (popular == true) recommendationResults[it.id]?.set("popular", broadcastService.getPopular(it, popularPage!!))
        }

        return recommendationResults
    }

    @Operation(summary = "Get similar, recommended, top, and popular broadcasts for a specific broadcast user if following")
    @GetMapping(path = ["/{broadcastId}"])
    fun getRecommendationsById(
            @PathVariable("userId") userId: UUID,
            @PathVariable("broadcastId") broadcastId: Int,
            @RequestParam(defaultValue = "false") similar: Boolean? = false,
            @RequestParam(value = "similar_page", defaultValue = "1") similarPage: Int? = 1,
            @RequestParam(defaultValue = "false") recommendations: Boolean? = false,
            @RequestParam(value = "recommendations_page", defaultValue = "1") recommendationsPage: Int? = 1,
            @RequestParam(defaultValue = "false") top: Boolean? = false,
            @RequestParam(value = "top_page", defaultValue = "1") topPage: Int? = 1,
            @RequestParam(defaultValue = "false") popular: Boolean? = false,
            @RequestParam(value = "popular_page", defaultValue = "1") popularPage: Int? = 1,
    ): Map<String, List<Broadcast>> {
        val serializedBroadcast = userService.getSerializedBroadcast(userId, broadcastId)
        val recommendationResult: MutableMap<String, List<Broadcast>> = mutableMapOf()

        if (similar == true) recommendationResult["similar"] = broadcastService.getSimilar(serializedBroadcast, similarPage!!)
        if (recommendations == true) recommendationResult["recommendations"] = broadcastService.getRecommended(serializedBroadcast, recommendationsPage!!)
        if (top == true) recommendationResult["top"] = broadcastService.getTopRated(serializedBroadcast, topPage!!)
        if (popular == true) recommendationResult["popular"] = broadcastService.getPopular(serializedBroadcast, popularPage!!)

        return recommendationResult
    }

}