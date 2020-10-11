package me.oriharel.seriesmanager.api.broadcast

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.service.BroadcastService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/recommend", produces = ["application/json"], consumes = ["application/json"])
class RecommendationsController @Autowired constructor(private val broadcastService: BroadcastService) {
    @Operation(summary = "Get the top or popular broadcasts")
    @GetMapping
    fun getRecommendations(
            @RequestParam(defaultValue = "false") tv: Boolean? = false,
            @RequestParam(defaultValue = "false") movie: Boolean? = false,
            @RequestParam(defaultValue = "false") top: Boolean? = false,
            @RequestParam(value = "top_page", defaultValue = "1") topPage: Int? = 1,
            @RequestParam(defaultValue = "false") popular: Boolean? = false,
            @RequestParam(value = "popular_page", defaultValue = "1") popularPage: Int? = 1,
    ): MutableMap<String, MutableMap<String, List<Broadcast>>> {
        val recommendationResults: MutableMap<String, MutableMap<String, List<Broadcast>>> = mutableMapOf()

        listOf("movie", "tv").forEach {
            if ((it == "movie" && movie == true) || (it == "tv" && tv == true)) {
                recommendationResults[it] = mutableMapOf()
                if (top == true) recommendationResults[it]?.set("top", broadcastService.getTopRated(SearchType.Movie, topPage!!))
                if (popular == true) recommendationResults[it]?.set("popular", broadcastService.getPopular(SearchType.Movie, popularPage!!))
            }
        }

        return recommendationResults
    }

    @Operation(summary = "Get similar, recommended, top, and popular broadcasts for a specific broadcast user if following")
    @GetMapping(path = ["/{type}/{broadcastId}"])
    fun getRecommendationsById(
            @PathVariable("type") type: SearchType,
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
        val recommendationResult: MutableMap<String, List<Broadcast>> = mutableMapOf()
        val serializedBroadcast = UserSerializedBroadcast(type.name.toLowerCase(), broadcastId)

        if (similar == true) recommendationResult["similar"] = broadcastService.getSimilar(serializedBroadcast, similarPage!!)
        if (recommendations == true) recommendationResult["recommendations"] = broadcastService.getRecommended(serializedBroadcast, recommendationsPage!!)
        if (top == true) recommendationResult["top"] = broadcastService.getTopRated(serializedBroadcast, topPage!!)
        if (popular == true) recommendationResult["popular"] = broadcastService.getPopular(serializedBroadcast, popularPage!!)

        return recommendationResult
    }
}