package me.oriharel.seriesmanager.api.broadcast.multi

import io.swagger.v3.oas.annotations.Operation
import me.oriharel.seriesmanager.api.broadcast.BroadcastListController
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.service.BroadcastService
import me.oriharel.seriesmanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/v1/user/{userId}/broadcast/list", produces = ["application/json"], consumes = ["application/json"])
class MultiBroadcastListController @Autowired constructor(
        userService: UserService,
        broadcastService: BroadcastService
) : BroadcastListController(
        userService,
        broadcastService,
        SearchType.Multi
) {
    @Operation(summary = "Get all the names of all the content lists")
    @GetMapping
    override fun getLists(): MutableSet<String> {
        return super.getLists()
    }

    @Operation(summary = "Get all the broadcasts pertaining to a certain content list")
    @GetMapping("/{listName}")
    override fun getBroadcastsOfList(
            @PathVariable listName: String,
            @RequestParam detailed: Boolean,
    ): List<Any> {
        return super.getBroadcastsOfList(listName, detailed)
    }
}