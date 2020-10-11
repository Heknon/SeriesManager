package me.oriharel.seriesmanager.dao.user

import me.oriharel.seriesmanager.api.response.AuthRequest
import me.oriharel.seriesmanager.model.User
import me.oriharel.seriesmanager.model.content.UserSerializedBroadcast
import me.oriharel.seriesmanager.security.CurrentUser
import me.oriharel.seriesmanager.security.JwtUtility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.stream.Collectors

@Repository("userDao")
class UserDataAccessService : UserDao {

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var jwtUtility: JwtUtility

    @Autowired
    lateinit var authManager: AuthenticationManager

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun addUser(user: User): User {
        if (repository.existsByEmail(user.email)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User with this email already exists")
        } else if (repository.existsByUsername(user.username)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User with this username already exists")
        }

        val newUser = User(UUID.randomUUID(), user.username, passwordEncoder.encode(user.password), user.email, user.name, user.isAdmin, user.broadcasts)
        return repository.insert(newUser)
    }

    override fun getAllUsers(): List<User> {
        val users = repository.findAll()
        return if (CurrentUser.currentUserIsAdmin) users else listOf(CurrentUser.currentUser.dbUser)
    }

    override fun getUserById(id: UUID): Optional<User> {
        if (CurrentUser.currentUserIsAdmin || (CurrentUser.currentUserId ?: 0) == id) {
            return repository.findById(id)
        } else {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must use your own user ID!")
        }
    }

    override fun deleteUserById(id: UUID): Optional<User> {
        if (CurrentUser.currentUserIsAdmin || (CurrentUser.currentUserId ?: 0) == id) {
            val user = repository.findById(id)
            repository.deleteById(id)
            return user
        } else {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must use your own user ID!")
        }
    }

    override fun updateUserById(id: UUID, user: User): User {
        if (CurrentUser.currentUserIsAdmin || (CurrentUser.currentUserId ?: 0) == id) {
            val new = User(id, user)
            repository.save(new)
            return new
        } else {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must use your own user ID!")
        }
    }

    override fun getBroadcasts(id: UUID): Optional<Set<UserSerializedBroadcast>> {
        val user = getUserById(id)
        if (user.isEmpty) {
            return Optional.empty()
        }

        return Optional.of(user.get().broadcasts)
    }

    override fun getBroadcastById(id: UUID, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        val user = getUserById(id)
        if (user.isEmpty) {
            return Optional.empty()
        }
        return user.get().broadcasts.stream().filter {
            it.id == serializedBroadcast.id && it.searchType == serializedBroadcast.searchType
        }.findFirst()
    }

    override fun getBroadcastByIdMulti(id: UUID, broadcastId: Int): Optional<List<UserSerializedBroadcast>> {
        val user = getUserById(id)
        if (user.isEmpty) {
            return Optional.empty()
        }

        return Optional.of(user.get().broadcasts.stream().filter {
            it.id == broadcastId
        }.collect(Collectors.toList()))
    }

    override fun addBroadcast(id: UUID, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        val userOptional = getUserById(id)
        if (userOptional.isEmpty) {
            return Optional.empty()
        }
        val user = userOptional.get()
        user.broadcasts.add(broadcast)
        repository.save(user)
        return Optional.of(broadcast)
    }

    override fun updateBroadcast(id: UUID, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        val userOptional = getUserById(id)
        if (userOptional.isEmpty) {
            return Optional.empty()
        }
        val user = userOptional.get()
        user.broadcasts.removeIf { it.id == broadcast.id && it.type.equals(broadcast.type, ignoreCase = true) }
        val bc = UserSerializedBroadcast(broadcast.id!!, broadcast)
        user.broadcasts.add(bc)
        repository.save(user)
        return Optional.of(bc)
    }

    override fun deleteBroadcast(id: UUID, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        val userOptional = getUserById(id)
        if (userOptional.isEmpty) {
            return Optional.empty()
        }
        val user = userOptional.get()
        val bc = user.broadcasts.filter { it.id == serializedBroadcast.id && it.type.equals(serializedBroadcast.type, ignoreCase = true) }[0]
        user.broadcasts.remove(bc)
        repository.save(user)
        return Optional.of(bc)
    }

    override fun markBroadcastWatched(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean {
        // check if the movie has already been watched as to not go through the process of marking twice
        if (serializedBroadcast.isMovie
                && ((serializedBroadcast.watched?.count() ?: 0) > 1
                        || serializedBroadcast.watched?.get(1)?.contains(1) == true)) return false

        // check if the episode has already been watched as to not go through the process of marking twice
        val unwatchedEpisodes = mutableSetOf<Short>()
        episode.forEach {
            if (serializedBroadcast.watched?.get(season)?.contains(it) == false) unwatchedEpisodes.add(it)
        }

        serializedBroadcast.watched?.get(season)?.addAll(unwatchedEpisodes.toTypedArray())
                ?: serializedBroadcast.watched?.set(season, mutableSetOf(*unwatchedEpisodes.toTypedArray()))
        updateSerializedBroadcast(id, serializedBroadcast)
        return true
    }

    override fun markBroadcastUnwatched(id: UUID, serializedBroadcast: UserSerializedBroadcast, season: Short, vararg episode: Short): Boolean {
        // check if the movie is already unwatched stop to not go through the process of marking twice
        if (serializedBroadcast.isMovie && serializedBroadcast.watched?.get(1)?.contains(1) == false) return false

        // if the season key doesn't exist there are no episodes to remove
        if (serializedBroadcast.watched?.containsKey(season) == false) return false

        // check if the episode is unwatched to not go through the process of marking twice
        val watchedEpisodes = mutableSetOf<Short>()
        episode.forEach {
            if (serializedBroadcast.watched?.get(season)?.contains(it) == true) watchedEpisodes.add(it)
        }

        serializedBroadcast.watched?.get(season)?.removeAll(watchedEpisodes.toTypedArray())
        updateSerializedBroadcast(id, serializedBroadcast)
        return true
    }

    override fun addListToBroadcast(id: UUID, listName: String, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        val bcOp = getBroadcastById(id, serializedBroadcast)
        if (bcOp.isEmpty) return Optional.empty()
        val bc = bcOp.get()
        bc.lists.add(listName)
        return updateBroadcast(id, bc)
    }

    override fun removeListFromBroadcast(id: UUID, listName: String, serializedBroadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        val bcOp = getBroadcastById(id, serializedBroadcast)
        if (bcOp.isEmpty) return Optional.empty()
        val bc = bcOp.get()
        bc.lists.remove(listName)
        return updateBroadcast(id, bc)
    }

    override fun generateJwtToken(authRequest: AuthRequest): String {
        try {
            authManager.authenticate(UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password))
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password")
        }

        return jwtUtility.generateToken(authRequest.username, if (authRequest.stayLoggedIn == true) 1000000 else 10)
    }

    override fun getSerializedBroadcastsOfList(id: UUID, listName: String): List<UserSerializedBroadcast> {
        return getUserById(id).get().broadcasts.filter { it.lists.contains(listName) }
    }

    fun updateSerializedBroadcast(id: UUID, serializedBroadcast: UserSerializedBroadcast) {
        val userOptional = getUserById(id)
        if (userOptional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with ID: $id")
        val user = userOptional.get()
        user.broadcasts.removeIf { it.id == serializedBroadcast.id && it.type == serializedBroadcast.type }
        user.broadcasts.add(serializedBroadcast)
        repository.save(user)
    }

}