package me.oriharel.seriesmanager.api.broadcast.movie

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriesmanager.api.broadcast.RecommendationsController
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.service.BroadcastService
import me.oriharel.seriesmanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/movie/recommend", produces = ["application/json"])
class MovieRecommendationsController @Autowired constructor(
        userService: UserService,
        broadcastService: BroadcastService,
) : RecommendationsController(
        userService,
        broadcastService,
        SearchType.Movie
) {
    @Operation(summary = "Get similar, recommended, top, and popular movies for each movie user is following")
    @GetMapping
    override fun getRecommendations(
            @RequestParam(defaultValue = "false") similar: Boolean?,
            @RequestParam(value = "similar_page", defaultValue = "1") similarPage: Int?,
            @RequestParam(defaultValue = "false") recommendations: Boolean?,
            @RequestParam(value = "recommendations_page", defaultValue = "1") recommendationsPage: Int?,
            @RequestParam(defaultValue = "false") top: Boolean?,
            @RequestParam(value = "top_page", defaultValue = "1") topPage: Int?,
            @RequestParam(defaultValue = "false") popular: Boolean?,
            @RequestParam(value = "popular_page", defaultValue = "1") popularPage: Int?,
    ): MutableMap<Int, MutableMap<String, List<Broadcast>>> {
        return super.getRecommendations(
                similar,
                similarPage,
                recommendations,
                recommendationsPage,
                top,
                topPage,
                popular,
                popularPage
        )
    }

    @Operation(summary = "Get similar, recommended, top, and popular movies for a specific movie")
    @GetMapping(path = ["/{broadcastId}"])
    override fun getRecommendationsById(
            @PathVariable("broadcastId") broadcastId: Int,
            @RequestParam(defaultValue = "false") similar: Boolean?,
            @RequestParam(value = "similar_page", defaultValue = "1") similarPage: Int?,
            @RequestParam(defaultValue = "false") recommendations: Boolean?,
            @RequestParam(value = "recommendations_page", defaultValue = "1") recommendationsPage: Int?,
            @RequestParam(defaultValue = "false") top: Boolean?,
            @RequestParam(value = "top_page", defaultValue = "1") topPage: Int?,
            @RequestParam(defaultValue = "false") popular: Boolean?,
            @RequestParam(value = "popular_page", defaultValue = "1") popularPage: Int?,
    ): Map<String, List<Broadcast>> {
        return super.getRecommendationsById(
                broadcastId,
                similar,
                similarPage,
                recommendations,
                recommendationsPage,
                top,
                topPage,
                popular,
                popularPage
        )
    }
}