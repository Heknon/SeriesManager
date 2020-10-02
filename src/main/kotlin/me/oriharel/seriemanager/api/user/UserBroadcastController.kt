package me.oriharel.seriemanager.api.user

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriemanager.api.response.GenericBroadcastStatus
import me.oriharel.seriemanager.api.response.WatchStatus
import me.oriharel.seriemanager.api.response.WatchStatusBulk
import me.oriharel.seriemanager.dao.broadcast.SearchType
import me.oriharel.seriemanager.model.content.Broadcast
import me.oriharel.seriemanager.model.content.Episode
import me.oriharel.seriemanager.model.content.Season
import me.oriharel.seriemanager.model.content.UserSerializedBroadcast
import me.oriharel.seriemanager.service.BroadcastService
import me.oriharel.seriemanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull


@RestController
@RequestMapping("api/v1/user/{userId}/broadcast")
class UserBroadcastController @Autowired constructor(private val userService: UserService, private val broadcastService: BroadcastService) {
    @Operation(summary = "Get all the serialized broadcasts a user is following")
    @GetMapping
    fun getBroadcasts(@PathVariable("userId") userId: UUID): Optional<Set<UserSerializedBroadcast>> {
        return userService.getBroadcasts(userId)
    }

    @Operation(summary = "Find new broadcasts")
    @GetMapping(path = ["/search/{type}"])
    fun searchBroadcasts(@PathVariable("type") type: SearchType, @RequestParam page: Int, @RequestParam query: String, @RequestParam adult: Boolean): List<Broadcast?> {
        return broadcastService.findBroadcasts(type, query, page, adult)
    }

    @Operation(summary = "Get all the detailed broadcasts a user is following")
    @GetMapping(path = ["/detailed"])
    fun getDetailedBroadcasts(@PathVariable("userId") userId: UUID): List<Broadcast?> {
        val optional = userService.getBroadcasts(userId)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Broadcasts not found!")
        return broadcastService.getDetailedBroadcasts(*optional.get().toTypedArray()).map {
            if (it.isEmpty) return@map null
            else it.get()
        }
    }

    @Operation(summary = "Gets a specific broadcast a user is following")
    @GetMapping(path = ["/{id}"])
    fun getBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): UserSerializedBroadcast {
        return getSerializedBroadcast(userId, id)
    }

    @Operation(summary = "Gets a specific broadcast a user if following with extra detail")
    @GetMapping(path = ["/{id}/detailed"])
    fun getDetailedBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): Broadcast {
        val sb = getSerializedBroadcast(userId, id)
        val optionalBC: Optional<Broadcast> = broadcastService.getDetailedBroadcast(sb)
        if (optionalBC.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong TMDB ID!")
        return optionalBC.get()
    }

    @Operation(summary = "Add a broadcast to a user's following")
    @PostMapping
    fun addBroadcast(@PathVariable("userId") userId: UUID, @NotNull @Valid @RequestBody serializedBroadcast: UserSerializedBroadcast): UserSerializedBroadcast {
        val optional = userService.addBroadcast(userId, serializedBroadcast)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @Operation(summary = "update a broadcast in a user's following")
    @PutMapping(path = ["/{id}"])
    fun updateBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int, @NotNull @Valid @RequestBody serializedBroadcast: UserSerializedBroadcast): UserSerializedBroadcast {
        val optional = userService.updateBroadcast(userId, id, serializedBroadcast)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @Operation(summary = "remove a broadcast from the user's following")
    @DeleteMapping(path = ["/{id}"])
    fun deleteBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): UserSerializedBroadcast {
        val optional = userService.deleteBroadcast(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @Operation(summary = "Get the watch status (watched or not watched) of all the broadcasts the user is following.",
            description = "A show will have it's key be be the DetailedTVShow object with the sub key in the underlying map be the episode and it's value watched or not.")
    @GetMapping(path = ["/watchstatus"])
    fun getBroadcastsWatchStatus(@PathVariable("userId") userId: UUID): Map<String, MutableMap<Int, MutableList<Map<String, Boolean>>>> {
        return broadcastService.getBroadcastsWatchStatus(*userService.getBroadcasts(userId).get().toTypedArray())
    }

    @Operation(summary = "Set the status of a broadcast a user is following as watched or unwatched.")
    @PostMapping(path = ["/{id}"])
    fun setWatchedStatus(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int, @NotNull @Valid @RequestBody watchStatus: WatchStatus): MutableMap<String, Boolean> {
        val sb = getSerializedBroadcast(userId, id)

        if (watchStatus.watched) {
            userService.markBroadcastWatched(userId, sb, watchStatus.season, watchStatus.episode)
        } else {
            userService.markBroadcastUnwatched(userId, sb, watchStatus.season, watchStatus.episode)
        }

        return mutableMapOf(Pair("success", true))
    }

    @Operation(summary = "Set the status of a broadcast a user is following as watched or unwatched in bulk.")
    @PostMapping(path = ["/{id}/bulk"])
    fun setWatchedStatus(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int, @NotNull @Valid @RequestBody watchStatus: WatchStatusBulk): MutableMap<String, Boolean> {
        val sb = getSerializedBroadcast(userId, id)

        if (watchStatus.watched) {
            userService.markBroadcastWatched(userId, sb, watchStatus.season, *watchStatus.episodes)
        } else {
            userService.markBroadcastUnwatched(userId, sb, watchStatus.season, *watchStatus.episodes)
        }

        return mutableMapOf(Pair("success", true))
    }

    @Operation(summary = "Get the status of a broadcast",
            method = "getStatus",
            description = "Returns JSON with the appropriate information.\n" +
                    "When a movie is passed you are told whether it was watched.\n" +
                    "When a show is passed with a season number and episode number you get the watch status of the episode, remaining episodes in the season and remaining episodes in the show\n" +
                    "When a show is passed with only a season number you get whether the user watched the whole season, remaining episodes in the season and remaining episodes in the show\n" +
                    "When a show is passed without a season number or episode number you get whether the user watched the whole show and the number of remaining episodes in the show")
    @GetMapping(path = ["/{id}/status"])
    fun getStatus(
            @PathVariable("userId") userId: UUID,
            @PathVariable("id") id: Int,
            @RequestParam season: Int?,
            @RequestParam episode: Int?
    ): GenericBroadcastStatus {
        val sb = getSerializedBroadcast(userId, id)
        val bc = broadcastService.getDetailedBroadcast(sb).get()
        val status = GenericBroadcastStatus()
        val isEpisode = season != null && episode != null
        val isSeason = season != null && episode == null
        val isBroadcast = season == null || episode == null

        status.name = bc.name

        return when {
            sb.isMovie -> {
                status.broadcastWatched = broadcastService.movieIsWatched(sb)
                status
            }
            isEpisode -> {
                val seasonDetails = broadcastService.getDetailedSeason(sb, season!!)
                status.broadcastWatched = broadcastService.episodeIsWatched(sb, season.toShort(), episode!!.toShort())
                status.remainingSeasonEpisodes = broadcastService.seasonEpisodesRemaining(sb, season.toShort())
                status.remainingShowEpisodes = broadcastService.showEpisodesRemaining(sb)
                status.episodeName = seasonDetails.episodes?.get(episode - 1)?.name ?: ""
                status.seasonName = seasonDetails.name
                status
            }
            isSeason -> {
                val seasonDetails = broadcastService.getDetailedSeason(sb, season!!)
                status.remainingSeasonEpisodes = broadcastService.seasonEpisodesRemaining(sb, season.toShort())
                status.broadcastWatched = status.remainingSeasonEpisodes == 0
                status.remainingShowEpisodes = broadcastService.showEpisodesRemaining(sb)
                status.seasonName = seasonDetails.name
                status
            }
            isBroadcast -> {
                status.remainingShowEpisodes = broadcastService.showEpisodesRemaining(sb)
                status.broadcastWatched = status.remainingShowEpisodes == 0
                status
            }
            else -> status
        }
    }

    @Operation(summary = "Get in depth details about a specific season in a show")
    @GetMapping("/{id}/season/{season}")
    fun getDetailedSeason(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int, @PathVariable("season") season: Int): Season {
        return broadcastService.getDetailedSeason(getSerializedBroadcast(userId, id), season)
    }

    @Operation(summary = "Get in depth details about a specific episode in a show")
    @GetMapping("/{id}/season/{season}/episode/{episode}")
    fun getDetailedEpisode(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int, @PathVariable("season") season: Int, @PathVariable("episode") episode: Int): Episode {
        return broadcastService.getDetailedEpisode(getSerializedBroadcast(userId, id), season, episode)
    }

    fun getSerializedBroadcast(userId: UUID, broadcastId: Int): UserSerializedBroadcast {
        val optional = userService.getBroadcastById(userId, broadcastId)
        if (optional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        }
        return optional.get()
    }
}