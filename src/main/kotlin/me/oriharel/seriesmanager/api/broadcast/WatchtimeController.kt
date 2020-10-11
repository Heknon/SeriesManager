package me.oriharel.seriesmanager.api.broadcast

import me.oriharel.seriesmanager.api.response.WatchtimeFormat
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.security.CurrentUser
import me.oriharel.seriesmanager.service.BroadcastService

abstract class WatchtimeController constructor(
        private val broadcastService: BroadcastService,
        private val searchType: SearchType,
) {
    open fun getMaxBroadcastWatchtime(
            broadcastId: Int,
    ): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getMaxShowWatchtime(
                CurrentUser.currentUserIdMustBeLoggedIn,
                broadcastId,
                searchType
        ))
    }

    open fun getMaxSeasonWatchtime(
            broadcastId: Int,
            seasonNumber: Int,
    ): WatchtimeFormat {
        return WatchtimeFormat.build(broadcastService.getMaxSeasonWatchtime(
                CurrentUser.currentUserIdMustBeLoggedIn,
                broadcastId,
                searchType,
                seasonNumber
        ))
    }

    open fun getBroadcastWatchtime(
            broadcastId: Int,
    ): WatchtimeFormat {
        return WatchtimeFormat.build(
                broadcastService.getShowWatchtime(
                        broadcastId = broadcastId,
                        searchType = searchType
                )
        )
    }

    open fun getSeasonWatchtime(
            broadcastId: Int,
            seasonNumber: Int,
    ): WatchtimeFormat {
        return WatchtimeFormat.build(
                broadcastService.getSeasonWatchtime(
                        broadcastId = broadcastId,
                        searchType = searchType,
                        season = seasonNumber
                ))
    }
}