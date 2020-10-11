package me.oriharel.seriesmanager.api.broadcast

import me.oriharel.seriesmanager.service.BroadcastService
import me.oriharel.seriesmanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/v1/user/{userId}/broadcast/list", produces = ["application/json"], consumes = ["application/json"])
class UserBroadcastListController @Autowired constructor(
        private val userService: UserService,
        private val broadcastService: BroadcastService,
) {
    @GetMapping
    fun getLists(@PathVariable userId: UUID): MutableSet<String> {
        val serializedBroadcasts = userService.getBroadcasts(userId).get()

        val result: MutableSet<String> = mutableSetOf()
        serializedBroadcasts.forEach {
            result.addAll(it.lists)
        }

        return result
    }

    @PostMapping("/{listName}/{broadcastId}/{searchType}")
    fun addListToShow(
            @PathVariable userId: UUID,
            @PathVariable listName: String,
            @PathVariable broadcastId: Int,
    ) {
        throw
        return
    }
}