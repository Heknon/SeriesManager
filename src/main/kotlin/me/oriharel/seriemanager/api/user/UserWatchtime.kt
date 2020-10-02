package me.oriharel.seriemanager.api.user

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriemanager.api.response.WatchtimeFormat
import me.oriharel.seriemanager.service.BroadcastService
import me.oriharel.seriemanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("api/v1/user/{userId}/watchtime", produces = ["application/json"], consumes = ["application/json"])
class UserWatchtime @Autowired constructor(
        private val userService: UserService,
        private val broadcastService: BroadcastService,
) {
    @Operation(summary = "Get the maximum watchtime attainable")
    @GetMapping(path = ["/max"])
    fun getMaxWatchtime(@PathVariable("userId") userId: UUID): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getMaxWatchtime(userId, userService))
    }

    @Operation(summary = "Get the maximum total watchtime attainable for a show")
    @GetMapping(path = ["/max/{showId}"])
    fun getMaxShowWatchtime(
            @PathVariable("userId") userId: UUID,
            @PathVariable("showId") showId: Int,
    ): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getMaxShowWatchtime(userId, userService.getSerializedBroadcast(userId, showId)))
    }

    @Operation(summary = "Get the maximum total watchtime attainable for a season in a show")
    @GetMapping(path = ["/max/{showId}/season/{seasonNumber}"])
    fun getMaxSeasonWatchtime(
            @PathVariable("userId") userId: UUID,
            @PathVariable("showId") showId: Int,
            @PathVariable("seasonNumber") seasonNumber: Int,
    ): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getMaxSeasonWatchtime(userId, userService.getSerializedBroadcast(userId, showId), seasonNumber))
    }

    @Operation(summary = "Get the user's total watchtime")
    @GetMapping
    fun getWatchtime(@PathVariable("userId") userId: UUID): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getWatchtime(userId, userService))
    }

    @Operation(summary = "Get the user's total watchtime for a show")
    @GetMapping(path = ["/{showId}"])
    fun getShowWatchtime(
            @PathVariable("userId") userId: UUID,
            @PathVariable("showId") showId: Int,
    ): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getShowWatchtime(userId, userService.getSerializedBroadcast(userId, showId)))
    }

    @Operation(summary = "Get the user's total watchtime for a season in a show")
    @GetMapping(path = ["/{showId}/season/{seasonNumber}"])
    fun getSeasonWatchtime(
            @PathVariable("userId") userId: UUID,
            @PathVariable("showId") showId: Int,
            @PathVariable("seasonNumber") seasonNumber: Int,
    ): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getSeasonWatchtime(userId, userService.getSerializedBroadcast(userId, showId), seasonNumber))
    }

}