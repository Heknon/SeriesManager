package me.oriharel.seriesmanager.api.broadcast.movie

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriesmanager.api.broadcast.BroadcastController
import me.oriharel.seriesmanager.api.response.GenericBroadcastStatus
import me.oriharel.seriesmanager.api.response.WatchStatus
import me.oriharel.seriesmanager.api.response.WatchStatusBulk
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.model.content.Episode
import me.oriharel.seriesmanager.model.content.Season
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.service.BroadcastService
import me.oriharel.seriesmanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull


@RestController
@RequestMapping("api/v1/movie/broadcast")
class MovieBroadcastController @Autowired constructor(
        userService: UserService,
        broadcastService: BroadcastService
) : BroadcastController(
        userService,
        broadcastService,
        SearchType.Movie
) {
    @Operation(summary = "Find new movies")
    @GetMapping(path = ["/search"])
    override fun searchBroadcasts(
            @RequestParam page: Int,
            @RequestParam query: String,
            @RequestParam adult: Boolean,
    ): List<Broadcast> {
        return super.searchBroadcasts(page, query, adult)
    }

    @Operation(summary = "Get the status of a broadcast",
            method = "getStatus",
            description = "Returns JSON with the appropriate information.\n" +
                    "When a movie is passed you are told whether it was watched.")
    @GetMapping(path = ["/{broadcastId}/status"])
    override fun getStatus(
            @PathVariable broadcastId: Int,
            @RequestParam season: Int?,
            @RequestParam episode: Int?,
    ): GenericBroadcastStatus {
        return super.getStatus(broadcastId, season, episode)
    }

    @Operation(summary = "Set the status of a movie a user is following as watched or unwatched.")
    @PostMapping(path = ["/{broadcastId}"])
    override fun setWatchedStatus(
            @PathVariable("broadcastId") broadcastId: Int,
            @NotNull @Valid @RequestBody watchStatus: WatchStatus,
    ): Map<String, Boolean> {
        return super.setWatchedStatus(broadcastId, watchStatus)
    }

    @Operation(summary = "Set the status of a movie a user is following as watched or unwatched in bulk.")
    @PostMapping(path = ["/{broadcastId}/bulk"])
    override fun setWatchedStatus(
            @PathVariable("broadcastId") broadcastId: Int,
            @NotNull @Valid @RequestBody watchStatus: WatchStatusBulk,
    ): Map<String, Boolean> {
        return super.setWatchedStatus(broadcastId, watchStatus)
    }

    @Operation(summary = "Get all the serialized movies a user is following")
    @GetMapping
    override fun getBroadcasts(): List<UserSerializedBroadcast> {
        return super.getBroadcasts()
    }

    @Operation(summary = "Get all the detailed movies a user is following")
    @GetMapping(path = ["/detailed"])
    override fun getDetailedBroadcasts(): List<Broadcast> {
        return super.getDetailedBroadcasts()
    }

    @Operation(summary = "Gets a specific movie a user is following")
    @GetMapping(path = ["/{broadcastId}"])
    override fun getBroadcast(
            @PathVariable("broadcastId") broadcastId: Int,
    ): UserSerializedBroadcast {
        return super.getBroadcast(broadcastId)
    }

    @Operation(summary = "Gets a specific movie a user is following with extra detail")
    @GetMapping(path = ["/{broadcastId}/detailed"])
    override fun getDetailedBroadcast(
            @PathVariable("broadcastId") broadcastId: Int,
    ): Broadcast {
        return super.getDetailedBroadcast(broadcastId)
    }

    @Operation(summary = "update a movie in a user's following")
    @PutMapping
    override fun updateBroadcast(
            @NotNull @Valid @RequestBody serializedBroadcast: UserSerializedBroadcast,
    ): UserSerializedBroadcast {
        return super.updateBroadcast(serializedBroadcast)
    }

    @Operation(summary = "remove a movie from the user's following")
    @DeleteMapping(path = ["/{broadcastId}"])
    override fun deleteBroadcast(
            @PathVariable("broadcastId") broadcastId: Int,
    ): UserSerializedBroadcast {
        return super.deleteBroadcast(broadcastId)
    }

    @Operation(summary = "Get the watch status (watched or not watched) of all the movies the user is following.")
    @GetMapping(path = ["/watchstatus"])
    override fun getBroadcastsWatchStatus(): Map<String, MutableMap<Int, MutableList<Map<String, Boolean>>>> {
        return super.getBroadcastsWatchStatus()
    }
}