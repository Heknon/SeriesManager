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
    fun searchBroadcasts(@PathVariable("type") type: SearchType, @RequestParam page: Int, @RequestParam query: String, @RequestParam adult: Boolean): List<Broadcast?> {
        return broadcastService.findBroadcasts(type, query, page, adult)
    }

    @Operation(summary = "Gets a specific broadcast")
    @GetMapping(path = ["/{type}/{id}"])
    fun getDetailedBroadcast(@PathVariable("type") type: SearchType, @PathVariable("id") id: Int): Broadcast {
        if (type == SearchType.Multi) throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Cannot use multi search type here.")
        val optional: Optional<Broadcast> = broadcastService.getDetailedBroadcast(UserSerializedBroadcast(type.name.toLowerCase(), id))
        if (optional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong TMDB ID!")
        }
        return optional.get()
    }

    @Operation(summary = "Get in depth details about a specific season in a show")
    @GetMapping("/{id}/season/{season}")
    fun getDetailedSeason(@PathVariable("id") id: Int, @PathVariable("season") season: Int): Season {
        return broadcastService.getDetailedSeason(UserSerializedBroadcast("tv", id), season)
    }

    @Operation(summary = "Get in depth details about a specific episode in a show")
    @GetMapping("/{id}/season/{season}/episode/{episode}")
    fun getDetailedEpisode(@PathVariable("id") id: Int, @PathVariable("season") season: Int, @PathVariable("episode") episode: Int): Episode {
        return broadcastService.getDetailedEpisode(UserSerializedBroadcast("tv", id), season, episode)
    }
}