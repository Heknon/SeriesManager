package me.oriharel.seriemanager.api.user

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriemanager.api.data.WatchStatus
import me.oriharel.seriemanager.dao.broadcast.SearchType
import me.oriharel.seriemanager.model.content.Broadcast
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
        val optional = userService.getBroadcastById(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @Operation(summary = "Gets a specific broadcast a user if following with extra detail")
    @GetMapping(path = ["/{id}/detailed"])
    fun getDetailedBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): Broadcast {
        val optional = userService.getBroadcastById(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Broadcast not found!")
        val optionalBC: Optional<Broadcast> = broadcastService.getDetailedBroadcast(optional.get())
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong TMDB ID!")
        return optionalBC.get()
    }

    @Operation(summary = "")
    @PostMapping
    fun addBroadcast(@PathVariable("userId") userId: UUID, @NotNull @Valid @RequestBody serializedBroadcast: UserSerializedBroadcast): UserSerializedBroadcast {
        val optional = userService.addBroadcast(userId, serializedBroadcast)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @Operation(summary = "")
    @PutMapping(path = ["/{id}"])
    fun updateBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int, @NotNull @Valid @RequestBody serializedBroadcast: UserSerializedBroadcast): UserSerializedBroadcast {
        val optional = userService.updateBroadcast(userId, id, serializedBroadcast)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @Operation(summary = "")
    @DeleteMapping(path = ["/{id}"])
    fun deleteBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): UserSerializedBroadcast {
        val optional = userService.deleteBroadcast(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @Operation(summary = "Get the watch status (watched or not watched) of all the broadcasts the user is following.",
            description = "A show will have it's key be be the DetailedTVShow object with the sub key in the underlying map be the episode and it's value watched or not.")
    @GetMapping(path = ["/watchstatus"])
    fun getBroadcastsWatchStatus(@PathVariable("userId") userId: UUID): Map<Broadcast, Map<Broadcast, Boolean>> {
        return broadcastService.getBroadcastsWatchStatus(*userService.getBroadcasts(userId).get().toTypedArray())
    }

    @Operation(summary = "Set the status of a broadcast a user is following as watched or unwatched.")
    @PostMapping(path = ["/{id}"])
    fun setWatchedStatus(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int, @NotNull @Valid @RequestBody watchStatus: WatchStatus) {
        val optional = userService.getBroadcastById(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        val sb = optional.get()

        if (sb.type.equals("movie", ignoreCase = true)
                && (watchStatus.season != 1.toShort() || watchStatus.episode != 1.toShort()))
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "When marking a movie as watched/unwatched you must set season=1 and episode=1")
        if (watchStatus.watched) {
            userService.markBroadcastWatched(userId, sb, watchStatus.season, watchStatus.episode)
        } else {
            userService.markBroadcastUnwatched(userId, sb, watchStatus.season, watchStatus.episode)
        }
    }
}