package me.oriharel.seriemanager.dao.user

import me.oriharel.seriemanager.model.User
import me.oriharel.seriemanager.model.content.UserSerializedBroadcast
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Repository("userDao")
class UserDataAccessService : UserDao {

    @Autowired
    lateinit var repository: UserRepository

    override fun addUser(user: User): User {
        return repository.insert(User(UUID.randomUUID(), user))
    }

    override fun getAllUsers(): List<User> {
        return repository.findAll()
    }

    override fun getUserById(id: UUID): Optional<User> {
        return repository.findById(id)
    }

    override fun deleteUserById(id: UUID): Optional<User> {
        val user = repository.findById(id)
        repository.deleteById(id)
        return user
    }

    override fun updateUserById(id: UUID, user: User): User {
        val new = User(id, user)
        repository.save(new)
        return new
    }

    override fun getBroadcasts(id: UUID): Optional<Set<UserSerializedBroadcast>> {
        val user = getUserById(id)
        if (user.isEmpty) {
            return Optional.empty()
        }
        return Optional.of(user.get().broadcasts)
    }

    override fun getBroadcastById(id: UUID, broadcastId: Int): Optional<UserSerializedBroadcast> {
        val user = getUserById(id)
        if (user.isEmpty) {
            return Optional.empty()
        }
        return user.get().broadcasts.stream().filter { it.id == broadcastId }.findFirst()
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

    override fun updateBroadcast(id: UUID, broadcastId: Int, broadcast: UserSerializedBroadcast): Optional<UserSerializedBroadcast> {
        val userOptional = getUserById(id)
        if (userOptional.isEmpty) {
            return Optional.empty()
        }
        val user = userOptional.get()
        user.broadcasts.removeIf { it.id == broadcastId }
        val bc = UserSerializedBroadcast(broadcastId, broadcast)
        user.broadcasts.add(bc)
        repository.save(user)
        return Optional.of(bc)
    }

    override fun deleteBroadcast(id: UUID, broadcastId: Int): Optional<UserSerializedBroadcast> {
        val userOptional = getUserById(id)
        if (userOptional.isEmpty) {
            return Optional.empty()
        }
        val user = userOptional.get()
        val bc = user.broadcasts.filter { it.id == broadcastId }[0]
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

    fun updateSerializedBroadcast(id: UUID, serializedBroadcast: UserSerializedBroadcast) {
        val userOptional = getUserById(id)
        if (userOptional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with ID: $id")
        val user = userOptional.get()
        user.broadcasts.removeIf { it.id == serializedBroadcast.id && it.type == serializedBroadcast.type }
        user.broadcasts.add(serializedBroadcast)
        repository.save(user)
    }


}