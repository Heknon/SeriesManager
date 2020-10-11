package me.oriharel.seriesmanager.api.broadcast.multi

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
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull


@RestController
@RequestMapping("api/v1/broadcast")
class MultiBroadcastController @Autowired constructor(
        userService: UserService,
        broadcastService: BroadcastService,
) : BroadcastController(
        userService,
        broadcastService,
        SearchType.Multi
) {
    @Operation(summary = "Find new broadcasts")
    @GetMapping(path = ["/search"])
    override fun searchBroadcasts(
            @RequestParam page: Int,
            @RequestParam query: String,
            @RequestParam adult: Boolean,
    ): List<Broadcast> {
        return super.searchBroadcasts(page, query, adult)
    }

    @Operation(summary = "Get in depth details about a specific season in a show")
    @GetMapping("/{broadcastId}/season/{season}")
    override fun getDetailedSeason(
            @PathVariable("broadcastId") broadcastId: Int,
            @PathVariable("season") season: Int,
    ): Season {
        return super.getDetailedSeason(broadcastId, season)
    }

    @Operation(summary = "Get in depth details about a specific episode in a show")
    @GetMapping("/{broadcastId}/season/{season}/episode/{episode}")
    override fun getDetailedEpisode(
            @PathVariable("broadcastId") broadcastId: Int,
            @PathVariable("season") season: Int,
            @PathVariable("episode") episode: Int,
    ): Episode {
        return super.getDetailedEpisode(broadcastId, season, episode)
    }

    @Operation(summary = "Get the status of a broadcast",
            method = "getStatus",
            description = "Returns JSON with the appropriate information.\n" +
                    "You will get the statuses in a 'tv' and 'movie' json property since this is a multi request.\n" +
                    "When a movie is passed you are told whether it was watched.\n" +
                    "When a show is passed with a season number and episode number you get the watch status of the episode, remaining episodes in the season and remaining episodes in the show\n" +
                    "When a show is passed with only a season number you get whether the user watched the whole season, remaining episodes in the season and remaining episodes in the show\n" +
                    "When a show is passed without a season number or episode number you get whether the user watched the whole show and the number of remaining episodes in the show")
    @GetMapping(path = ["/{broadcastId}/status"])
    fun getStatuses(
            @PathVariable broadcastId: Int,
            @RequestParam season: Int?,
            @RequestParam episode: Int?,
    ): MutableMap<String, GenericBroadcastStatus> {
        val result: MutableMap<String, GenericBroadcastStatus> = mutableMapOf()
        result["tv"] = super.getStatus(broadcastId, season, episode, SearchType.Tv)
        result["movie"] = super.getStatus(broadcastId, season, episode, SearchType.Movie)
        return result
    }

    @Operation(summary = "Get all the serialized broadcasts a user is following")
    @GetMapping
    override fun getBroadcasts(): List<UserSerializedBroadcast> {
        return super.getBroadcasts()
    }

    @Operation(summary = "Get all the detailed broadcasts a user is following")
    @GetMapping(path = ["/detailed"])
    override fun getDetailedBroadcasts(): List<Broadcast> {
        return super.getDetailedBroadcasts()
    }

    @Operation(summary = "Add a broadcast to a user's following")
    @PostMapping
    override fun addBroadcast(
            @NotNull @Valid @RequestBody serializedBroadcast: UserSerializedBroadcast,
    ): UserSerializedBroadcast {
        return super.addBroadcast(serializedBroadcast)
    }

    @Operation(summary = "Get the watch status (watched or not watched) of all the broadcasts the user is following.",
            description = "A show will have it's key be be the DetailedTVShow object with the sub key in the underlying map be the episode and it's value watched or not.")
    @GetMapping(path = ["/watchstatus"])
    override fun getBroadcastsWatchStatus(): Map<String, MutableMap<Int, MutableList<Map<String, Boolean>>>> {
        return super.getBroadcastsWatchStatus()
    }
}