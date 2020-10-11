package me.oriharel.seriesmanager.api.broadcast

import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.security.CurrentUser
import me.oriharel.seriesmanager.service.BroadcastService
import me.oriharel.seriesmanager.service.UserService

abstract class BroadcastListController(
        private val userService: UserService,
        private val broadcastService: BroadcastService,
        private val searchType: SearchType,
) {
    open fun getLists(): MutableSet<String> {
        val serializedBroadcasts = userService.getBroadcasts(CurrentUser.currentUserIdMustBeLoggedIn)
                .get()
                .filter { it.searchType == searchType || searchType == SearchType.Multi }

        val result: MutableSet<String> = mutableSetOf()
        serializedBroadcasts.forEach {
            result.addAll(it.lists)
        }

        return result
    }

    open fun addListToBroadcast(
            listName: String,
            broadcastId: Int,
    ): UserSerializedBroadcast {
        return userService.addListToBroadcast(CurrentUser.currentUserIdMustBeLoggedIn, listName, UserSerializedBroadcast(broadcastId, searchType)).get()
    }

    open fun removeListFromBroadcast(
            listName: String,
            broadcastId: Int
    ) : UserSerializedBroadcast {
        return userService.removeListFromBroadcast(CurrentUser.currentUserIdMustBeLoggedIn, listName, UserSerializedBroadcast(broadcastId, searchType)).get()
    }

    open fun getBroadcastsOfList(listName: String, detailed: Boolean): List<Any> {
        val serializedBroadcasts = userService.getSerializedBroadcastsOfList(listName = listName)
                .filter { it.searchType == searchType || searchType == SearchType.Multi }
        if (detailed) return broadcastService.getDetailedBroadcasts(*serializedBroadcasts.toTypedArray())
        return serializedBroadcasts
    }
}