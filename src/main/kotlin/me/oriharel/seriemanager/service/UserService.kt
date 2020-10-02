package me.oriharel.seriemanager.service

import me.oriharel.seriemanager.dao.user.UserDao
import me.oriharel.seriemanager.model.User
import me.oriharel.seriemanager.model.content.UserSerializedBroadcast
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService @Autowired constructor(@Qualifier("userDao") private val userDao: UserDao) {
    fun addUser(user: User): User {
        return userDao.addUser(user)
    }

    fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    fun getUserById(id: UUID): Optional<User> {
        return userDao.getUserById(id)
    }

    fun deleteUserById(id: UUID): Optional<User> {
        return userDao.deleteUserById(id)
    }

    fun updateUserById(id: UUID, User: User): User {
        return userDao.updateUserById(id, User)
    }

    fun getBroadcasts(id: UUID): Optional<Set<UserSerializedBroadcast>> {
        return userDao.getBroadcasts(id)
    }

    fun getBroadcastById(id: UUID, broadcastId: Int): Optional<UserSerializedBroadcast> {
        return userDao.getBroadcastById(id, broadcastId)
    }

    fun addBroadcast(id: UUID, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        return userDao.addBroadcast(id, broadcast)
    }

    fun updateBroadcast(id: UUID, broadcastId: Int, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        return userDao.updateBroadcast(id, broadcastId, broadcast)
    }

    fun deleteBroadcast(id: UUID, broadcastId: Int): Optional<UserSerializedBroadcast> {
        return userDao.deleteBroadcast(id, broadcastId)
    }

    fun markBroadcastWatched(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean {
        return userDao.markBroadcastWatched(id, serializedBroadcast, season, *episode)
    }

    fun markBroadcastUnwatched(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean {
        return userDao.markBroadcastUnwatched(id, serializedBroadcast, season, *episode)
    }
}