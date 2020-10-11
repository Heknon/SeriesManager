package me.oriharel.seriesmanager.api.broadcast.multi

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriesmanager.api.response.WatchtimeFormat
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.security.CurrentUser
import me.oriharel.seriesmanager.service.BroadcastService
import me.oriharel.seriesmanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("api/v1/watchtime", produces = ["application/json"], consumes = ["application/json"])
class BroadcastWatchtimeController @Autowired constructor(
        private val userService: UserService,
        private val broadcastService: BroadcastService,
) {
    @Operation(summary = "Get the maximum watchtime attainable")
    @GetMapping(path = ["/max"])
    fun getMaxWatchtime(@PathVariable("userId") userId: UUID): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getMaxWatchtime(userId, userService))
    }

    @Operation(summary = "Get the user's total watchtime")
    @GetMapping
    fun getWatchtime(): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getWatchtime(userService = userService))
    }
}