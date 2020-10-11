package me.oriharel.seriesmanager.api.broadcast.tv

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
@RequestMapping("api/v1/tv/watchtime", produces = ["application/json"], consumes = ["application/json"])
class TVWatchtimeController @Autowired constructor(
        broadcastService: BroadcastService,
) : WatchtimeController(
        broadcastService,
        SearchType.Tv
) {
    @Operation(summary = "Get the maximum total watchtime attainable for a show")
    @GetMapping(path = ["/max/{broadcastId}"])
    override fun getMaxBroadcastWatchtime(
            @PathVariable("broadcastId") broadcastId: Int,
    ): WatchtimeFormat {
        return super.getMaxBroadcastWatchtime(broadcastId)
    }

    @Operation(summary = "Get the maximum total watchtime attainable for a season in a show")
    @GetMapping(path = ["/max/{broadcastId}/season/{seasonNumber}"])
    override fun getMaxSeasonWatchtime(
            @PathVariable("broadcastId") broadcastId: Int,
            @PathVariable("seasonNumber") seasonNumber: Int,
    ): WatchtimeFormat {
        return super.getMaxSeasonWatchtime(broadcastId, seasonNumber)
    }

    @Operation(summary = "Get the user's total watchtime for a show")
    @GetMapping(path = ["/{broadcastId}"])
    override fun getBroadcastWatchtime(
            @PathVariable("broadcastId") broadcastId: Int,
    ): WatchtimeFormat {
        return super.getBroadcastWatchtime(broadcastId)
    }

    @Operation(summary = "Get the user's total watchtime for a season in a show")
    @GetMapping(path = ["/{broadcastId}/season/{seasonNumber}"])
    override fun getSeasonWatchtime(
            @PathVariable("broadcastId") broadcastId: Int,
            @PathVariable("seasonNumber") seasonNumber: Int,
    ): WatchtimeFormat {
        return super.getSeasonWatchtime(broadcastId, seasonNumber)
    }
}