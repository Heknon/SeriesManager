package me.oriharel.seriesmanager.api.broadcast.movie

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriesmanager.api.broadcast.WatchtimeController
import me.oriharel.seriesmanager.api.response.WatchtimeFormat
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.service.BroadcastService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/movie/watchtime", produces = ["application/json"], consumes = ["application/json"])
class MovieWatchtimeController @Autowired constructor(
        broadcastService: BroadcastService,
) : WatchtimeController(
        broadcastService,
        SearchType.Movie
) {
    @Operation(summary = "Get the user's total watchtime for a movie")
    @GetMapping(path = ["/{broadcastId}"])
    override fun getBroadcastWatchtime(
            @PathVariable("broadcastId") broadcastId: Int,
    ): WatchtimeFormat {
        return super.getBroadcastWatchtime(broadcastId)
    }
}