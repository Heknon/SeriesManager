package me.oriharel.seriemanager.api.user

import me.oriharel.seriemanager.model.content.SerializedBroadcast
import me.oriharel.seriemanager.service.BroadcastService
import me.oriharel.seriemanager.service.UserService
import org.apache.coyote.Response
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.validation.*
import javax.validation.constraints.NotNull


@RestController
@RequestMapping("api/v1/user/{userId}/broadcast")
class UserBroadcastController @Autowired constructor(private val userService: UserService, private val broadcastService: BroadcastService) {
    @GetMapping
    fun getBroadcasts(@PathVariable("userId") userId: UUID): Optional<List<SerializedBroadcast>> {
        return userService.getBroadcasts(userId)
    }

    @GetMapping("/detailed")
    fun getDetailedBroadcasts(@PathVariable("userId") userId: UUID): List<MutableMap<String, Any>?> {
        val optional = userService.getBroadcasts(userId)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Broadcasts not found!")
        return broadcastService.getDetailedBroadcasts(*optional.get().toTypedArray()).map {
            if (it.isEmpty) return@map null
            else it.get().toMap()
        }
    }

    @GetMapping(path = ["/{id}"])
    fun getBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): SerializedBroadcast {
        val optional = userService.getBroadcastById(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    @GetMapping(path = ["/{id}/detailed"])
    fun getDetailedBroadcast(@PathVariable("userId") userId: UUID, @PathVariable("id") id: Int): MutableMap<String, Any>? {
        val optional = userService.getBroadcastById(userId, id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Broadcast not found!")
        val optionalBC = broadcastService.getDetailedBroadcast(optional.get())
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong TMDB ID!")
        return optionalBC.get().toMap()
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