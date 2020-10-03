package me.oriharel.seriemanager.api

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriemanager.dao.broadcast.SearchType
import me.oriharel.seriemanager.model.content.Broadcast
import me.oriharel.seriemanager.model.content.Episode
import me.oriharel.seriemanager.model.content.Season
import me.oriharel.seriemanager.model.content.UserSerializedBroadcast
import me.oriharel.seriemanager.service.BroadcastService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*


@RestController
@RequestMapping("api/v1/broadcast")
class BroadcastController @Autowired constructor(private val broadcastService: BroadcastService) {
    @Operation(summary = "Find new broadcasts")
    @GetMapping(path = ["/search/{type}"])
    fun searchBroadcasts(
            @PathVariable("type") type: SearchType,
            @RequestParam page: Int,
            @RequestParam query: String,
            @RequestParam adult: Boolean,
    ): List<Broadcast?> {
        return broadcastService.findBroadcasts(type, query, page, adult)
    }

    @Operation(summary = "Gets a specific broadcast")
    @GetMapping(path = ["/{type}/{broadcastId}"])
    fun getDetailedBroadcast(
            @PathVariable("type") type: SearchType,
            @PathVariable("broadcastId") broadcastId: Int,
    ): Any {
        return if (type == SearchType.Multi) {
            mutableListOf(
                    broadcastService.getDetailedBroadcast(UserSerializedBroadcast("tv", broadcastId)),
                    broadcastService.getDetailedBroadcast(UserSerializedBroadcast("movie", broadcastId))
            ).filter { it.isPresent }.map { it.get() }
        } else {
            val optional: Optional<Broadcast> = broadcastService.getDetailedBroadcast(UserSerializedBroadcast(type.name.toLowerCase(), broadcastId))
            if (optional.isEmpty) {
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong TMDB ID!")
            }
            optional.get()
        }
    }

    @Operation(summary = "Get in depth details about a specific season in a show")
    @GetMapping("/{broadcastId}/season/{season}")
    fun getDetailedSeason(
            @PathVariable("broadcastId") broadcastId: Int,
            @PathVariable("season") season: Int,
    ): Season {
        return broadcastService.getDetailedSeason(UserSerializedBroadcast("tv", broadcastId), season)
    }

    @Operation(summary = "Get in depth details about a specific episode in a show")
    @GetMapping("/{broadcastId}/season/{season}/episode/{episode}")
    fun getDetailedEpisode(
            @PathVariable("broadcastId") broadcastId: Int,
            @PathVariable("season") season: Int,
            @PathVariable("episode") episode: Int,
    ): Episode {
        return broadcastService.getDetailedEpisode(UserSerializedBroadcast("tv", broadcastId), season, episode)
    }
}