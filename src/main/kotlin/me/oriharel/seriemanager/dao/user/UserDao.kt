package me.oriharel.seriemanager.dao.user

import me.oriharel.seriemanager.model.User
import me.oriharel.seriemanager.model.content.UserSerializedBroadcast
import java.util.*

interface UserDao {
    fun addUser(user: User): User

    fun getAllUsers(): List<User>

    fun getUserById(id: UUID): Optional<User>

    fun deleteUserById(id: UUID): Optional<User>

    fun updateUserById(id: UUID, user: User): User

    fun getBroadcasts(id: UUID): Optional<Set<UserSerializedBroadcast>>

    fun getBroadcastById(id: UUID, broadcastId: Int): Optional<UserSerializedBroadcast>

    fun addBroadcast(id: UUID, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast>

    fun updateBroadcast(id: UUID, broadcastId: Int, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast>

    fun deleteBroadcast(id: UUID, broadcastId: Int): Optional<UserSerializedBroadcast>

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

}