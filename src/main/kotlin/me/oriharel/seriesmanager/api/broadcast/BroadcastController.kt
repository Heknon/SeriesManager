package me.oriharel.seriesmanager.api.broadcast

import me.oriharel.seriesmanager.api.response.GenericBroadcastStatus
import me.oriharel.seriesmanager.api.response.WatchStatus
import me.oriharel.seriesmanager.api.response.WatchStatusBulk
import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.content.Broadcast
import me.oriharel.seriesmanager.model.content.Episode
import me.oriharel.seriesmanager.model.content.Season
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.security.CurrentUser
import me.oriharel.seriesmanager.service.BroadcastService
import me.oriharel.seriesmanager.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull


abstract class BroadcastController constructor(
        private val userService: UserService,
        private val broadcastService: BroadcastService,
        private val searchType: SearchType,
) {
    open fun searchBroadcasts(
            page: Int,
            query: String,
            adult: Boolean,
    ): List<Broadcast> {
        return broadcastService.findBroadcasts(searchType, query, page, adult)
    }

    open fun getDetailedSeason(
            broadcastId: Int,
            season: Int,
    ): Season {
        return broadcastService.getDetailedSeason(UserSerializedBroadcast(broadcastId, SearchType.Tv), season)
    }

    open fun getDetailedEpisode(
            broadcastId: Int,
            season: Int,
            episode: Int,
    ): Episode {
        return broadcastService.getDetailedEpisode(UserSerializedBroadcast(broadcastId, SearchType.Tv), season, episode)
    }

    open fun getStatus(
            broadcastId: Int,
            season: Int?,
            episode: Int?,
    ): GenericBroadcastStatus {
        return getStatus(broadcastId, season, episode, searchType)
    }

    protected fun getStatus(
            broadcastId: Int,
            season: Int?,
            episode: Int?,
            searchType: SearchType,
    ): GenericBroadcastStatus {
        val sb = userService.getSerializedBroadcast(CurrentUser.currentUserIdMustBeLoggedIn, UserSerializedBroadcast(broadcastId, searchType))
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

    open fun setWatchedStatus(
            @PathVariable("broadcastId") broadcastId: Int,
            @NotNull @Valid @RequestBody watchStatus: WatchStatus,
    ): Map<String, Boolean> {
        val userId = CurrentUser.currentUserIdMustBeLoggedIn
        val sb = userService.getSerializedBroadcast(userId, UserSerializedBroadcast(broadcastId, searchType))

        if (watchStatus.watched) {
            userService.markBroadcastWatched(userId, sb, watchStatus.season, watchStatus.episode)
        } else {
            userService.markBroadcastUnwatched(userId, sb, watchStatus.season, watchStatus.episode)
        }

        return mutableMapOf(Pair("success", true))
    }

    open fun setWatchedStatus(
            @PathVariable("broadcastId") broadcastId: Int,
            @NotNull @Valid @RequestBody watchStatus: WatchStatusBulk,
    ): Map<String, Boolean> {
        val userId = CurrentUser.currentUserIdMustBeLoggedIn
        val sb = userService.getSerializedBroadcast(userId, UserSerializedBroadcast(broadcastId, searchType))

        if (watchStatus.watched) {
            userService.markBroadcastWatched(userId, sb, watchStatus.season, *watchStatus.episodes)
        } else {
            userService.markBroadcastUnwatched(userId, sb, watchStatus.season, *watchStatus.episodes)
        }

        return mutableMapOf(Pair("success", true))
    }

    open fun getBroadcasts(): List<UserSerializedBroadcast> {
        return userService.getBroadcasts(CurrentUser.currentUserIdMustBeLoggedIn).get()
                .filter { it.searchType == searchType || searchType == SearchType.Multi }
    }

    open fun getDetailedBroadcasts(): List<Broadcast> {
        val optional = userService.getBroadcasts(CurrentUser.currentUserIdMustBeLoggedIn)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Broadcasts not found!")
        return broadcastService.getDetailedBroadcasts(
                *optional.get()
                        .filter { it.searchType == searchType || searchType == SearchType.Multi }
                        .toTypedArray()
        ).mapNotNull {
            if (it.isPresent) it.get()
            else null
        }
    }

    open fun getBroadcast(
            broadcastId: Int,
    ): UserSerializedBroadcast {
        return userService.getSerializedBroadcast(CurrentUser.currentUserIdMustBeLoggedIn, UserSerializedBroadcast(broadcastId, searchType))
    }

    open fun getDetailedBroadcast(
            broadcastId: Int,
    ): Broadcast {
        val sb = userService.getSerializedBroadcast(CurrentUser.currentUserIdMustBeLoggedIn, UserSerializedBroadcast(broadcastId, searchType))
        val optionalBC: Optional<Broadcast> = broadcastService.getDetailedBroadcast(sb)
        if (optionalBC.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong TMDB ID!")
        return optionalBC.get()
    }

    open fun addBroadcast(
            @NotNull @Valid @RequestBody serializedBroadcast: UserSerializedBroadcast,
    ): UserSerializedBroadcast {
        val optional = userService.addBroadcast(
                CurrentUser.currentUserIdMustBeLoggedIn,
                UserSerializedBroadcast(serializedBroadcast.id!!, serializedBroadcast.searchType)
        )
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    open fun updateBroadcast(
            @NotNull @Valid @RequestBody serializedBroadcast: UserSerializedBroadcast,
    ): UserSerializedBroadcast {
        val optional = userService.updateBroadcast(CurrentUser.currentUserIdMustBeLoggedIn, serializedBroadcast)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    open fun deleteBroadcast(
            @PathVariable("broadcastId") broadcastId: Int,
    ): UserSerializedBroadcast {
        val optional = userService.deleteBroadcast(CurrentUser.currentUserIdMustBeLoggedIn, UserSerializedBroadcast(broadcastId, searchType))
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        return optional.get()
    }

    open fun getBroadcastsWatchStatus(): Map<String, MutableMap<Int, MutableList<Map<String, Boolean>>>> {
        return broadcastService
                .getBroadcastsWatchStatus(*userService.getBroadcasts(CurrentUser.currentUserIdMustBeLoggedIn).get()
                        .filter { it.searchType == searchType || searchType == SearchType.Multi }.toTypedArray()
                )
    }
}