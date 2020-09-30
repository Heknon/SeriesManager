package me.oriharel.seriemanager.api.user

import me.oriharel.seriemanager.dao.broadcast.SearchType
import me.oriharel.seriemanager.model.content.Broadcast
import me.oriharel.seriemanager.model.content.SerializedBroadcast
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
    @GetMapping
    fun getBroadcasts(@PathVariable("userId") userId: UUID): Optional<List<SerializedBroadcast>> {
        return userService.getBroadcasts(userId)
    }

    @GetMapping(path = ["/search/{type}"])
    fun searchBroadcasts(@PathVariable("type") type: SearchType, @RequestParam page: Int, @RequestParam query: String, @RequestParam adult: Boolean): List<Broadcast?> {
        return broadcastService.findBroadcasts(type, query, page, adult)
    }

    @GetMapping(path = ["/detailed"])
    fun getDetailedBroadcasts(@PathVariable("userId") userId: UUID): List<Broadcast?> {
        val optional = userService.getBroadcasts(userId)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Broadcasts not found!")
        return broadcastService.getDetailedBroadcasts<Broadcast>(*optional.get().toTypedArray()).map {
            if (it.isEmpty) return@map null
            else it.get()
        }
    }

    @GetMapping(path = ["/{id}"])
    fun getBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): SerializedBroadcast {
        val optional = userService.getBroadcastById(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @GetMapping(path = ["/{id}/detailed"])
    fun getDetailedBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): Broadcast {
        val optional = userService.getBroadcastById(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Broadcast not found!")
        val optionalBC: Optional<Broadcast> = broadcastService.getDetailedBroadcast(optional.get())
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong TMDB ID!")
        return optionalBC.get()
    }

    @PostMapping
    fun addBroadcast(@PathVariable("userId") userId: UUID, @NotNull @Valid @RequestBody serializedBroadcast: SerializedBroadcast): SerializedBroadcast {
        val optional = userService.addBroadcast(userId, serializedBroadcast)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @PutMapping(path = ["/{id}"])
    fun updateBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int, @NotNull @Valid @RequestBody serializedBroadcast: SerializedBroadcast): SerializedBroadcast {
        val optional = userService.updateBroadcast(userId, id, serializedBroadcast)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): SerializedBroadcast {
        val optional = userService.deleteBroadcast(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }
}