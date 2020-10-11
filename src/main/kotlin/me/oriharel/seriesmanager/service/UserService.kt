package me.oriharel.seriesmanager.service

import me.oriharel.seriesmanager.api.response.AuthRequest
import me.oriharel.seriesmanager.dao.user.UserDao
import me.oriharel.seriesmanager.model.User
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.security.CurrentUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class UserService @Autowired constructor(@Qualifier("userDao") private val userDao: UserDao) {

    fun addUser(user: User): User {
        return userDao.addUser(user)
    }

    fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    fun getUserById(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn): Optional<User> {
        return userDao.getUserById(userId)
    }

    fun deleteUserById(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn): Optional<User> {
        return userDao.deleteUserById(userId)
    }

    fun updateUserById(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn, User: User): User {
        return userDao.updateUserById(userId, User)
    }

    fun getBroadcasts(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn): Optional<Set<UserSerializedBroadcast>> {
        return userDao.getBroadcasts(userId)
    }

    fun getBroadcastById(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        return userDao.getBroadcastById(userId, serializedBroadcast)
    }

    fun getBroadcastByIdMulti(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn, broadcastId: Int): Optional<List<UserSerializedBroadcast>> {
        return userDao.getBroadcastByIdMulti(userId, broadcastId)
    }

    fun addBroadcast(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        return userDao.addBroadcast(userId, broadcast)
    }

    fun updateBroadcast(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        return userDao.updateBroadcast(userId, broadcast)
    }

    fun deleteBroadcast(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        return userDao.deleteBroadcast(userId, serializedBroadcast)
    }

    fun markBroadcastWatched(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn, serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean {
        if (serializedBroadcast.isMovie) {
            return userDao.markMovieWatched(userId, serializedBroadcast)
        }
        return userDao.markBroadcastWatched(userId, serializedBroadcast, season, *episode)
    }

    fun markBroadcastUnwatched(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn, serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean {
        if (serializedBroadcast.isMovie) {
            return userDao.markMovieUnwatched(userId, serializedBroadcast)
        }
        return userDao.markBroadcastUnwatched(userId, serializedBroadcast, season, *episode)
    }

    fun generateJwtToken(authRequest: AuthRequest): String {
        return userDao.generateJwtToken(authRequest)
    }

    fun addListToShow(userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn, listName: String, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        return userDao.addListToShow(userId, listName, serializedBroadcast)
    }

    fun getSerializedBroadcast(
            userId: UUID = CurrentUser.currentUserIdMustBeLoggedIn,
            serializedBroadcast: UserSerializedBroadcast,
    ): UserSerializedBroadcast {
        val optional = userDao.getBroadcastById(userId, serializedBroadcast)
        if (optional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have this broadcast!")
        }
        return optional.get()
    }
}