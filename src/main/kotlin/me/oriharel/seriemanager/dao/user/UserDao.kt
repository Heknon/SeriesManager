package me.oriharel.seriemanager.dao.user

import me.oriharel.seriemanager.model.User
import me.oriharel.seriemanager.model.content.SerializedBroadcast
import java.util.*

interface UserDao {
    fun addUser(user: User): User

    fun getAllUsers(): List<User>

    fun getUserById(id: UUID): Optional<User>

    fun deleteUserById(id: UUID): Optional<User>

    fun updateUserById(id: UUID, user: User): User

    fun getBroadcasts(id: UUID): Optional<List<SerializedBroadcast>>

    fun getBroadcastById(id: UUID, broadcastId: Int): Optional<SerializedBroadcast>

    fun addBroadcast(id: UUID, broadcast: SerializedBroadcast): Optional<SerializedBroadcast>

    fun updateBroadcast(id: UUID, broadcastId: Int, broadcast: SerializedBroadcast): Optional<SerializedBroadcast>

    fun deleteBroadcast(id: UUID, broadcastId: Int): Optional<SerializedBroadcast>

}