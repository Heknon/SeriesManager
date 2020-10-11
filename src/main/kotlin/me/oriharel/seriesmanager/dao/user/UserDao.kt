package me.oriharel.seriesmanager.dao.user

import me.oriharel.seriesmanager.api.response.AuthRequest
import me.oriharel.seriesmanager.model.User
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.security.CurrentUser
import java.util.*

interface UserDao {
    fun addUser(user: User): User

    fun getAllUsers(): List<User>

    fun getUserById(id: UUID): Optional<User>

    fun deleteUserById(id: UUID): Optional<User>

    fun updateUserById(id: UUID, user: User): User

    fun getBroadcasts(id: UUID): Optional<Set<UserSerializedBroadcast>>

    fun getBroadcastById(id: UUID, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast>

    fun getBroadcastByIdMulti(id: UUID, broadcastId: Int): Optional<List<UserSerializedBroadcast>>

    fun addBroadcast(id: UUID, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast>

    fun updateBroadcast(id: UUID, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast>

    fun deleteBroadcast(id: UUID, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast>

    /**
     * Marks specific place in broadcast as watched.
     * Pass season: 1, episode: 1 to mark movie as watched
     *
     * @return false if broadcast was already marked as watched
     */
    fun markBroadcastWatched(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean

    fun markMovieWatched(id: UUID, serializedBroadcast: UserSerializedBroadcast): Boolean {
        return markBroadcastWatched(id, serializedBroadcast, 1, 1)
    }

    /**
     * Marks specific place in broadcast as watched.
     * Pass season: 1, episode: 1 to mark movie as unwatched
     *
     * @return false if broadcast was already marked as unwatched
     */
    fun markBroadcastUnwatched(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean

    fun markMovieUnwatched(id: UUID, serializedBroadcast: UserSerializedBroadcast): Boolean {
        return markBroadcastUnwatched(id, serializedBroadcast, 1, 1)
    }

    fun addListToBroadcast(id: UUID, listName: String, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast>

    fun removeListFromBroadcast(id: UUID, listName: String, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast>

    fun generateJwtToken(authRequest: AuthRequest): String

    fun getSerializedBroadcastsOfList(id: UUID, listName: String): List<UserSerializedBroadcast>
}