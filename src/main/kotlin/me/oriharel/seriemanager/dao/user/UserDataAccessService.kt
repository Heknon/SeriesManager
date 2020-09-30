package me.oriharel.seriemanager.dao.user

import me.oriharel.seriemanager.model.User
import me.oriharel.seriemanager.model.content.SerializedBroadcast
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
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

    override fun getBroadcasts(id: UUID): Optional<List<SerializedBroadcast>> {
        val user = getUserById(id)
        if (user.isEmpty) {
            return Optional.empty()
        }
        return Optional.of(user.get().broadcasts)
    }

    override fun getBroadcastById(id: UUID, broadcastId: Int): Optional<SerializedBroadcast> {
        val user = getUserById(id)
        if (user.isEmpty) {
            return Optional.empty()
        }
        return user.get().broadcasts.stream().filter { it.id == broadcastId }.findFirst()
    }

    override fun addBroadcast(id: UUID, broadcast: SerializedBroadcast): Optional<SerializedBroadcast> {
        val userOptional = getUserById(id)
        if (userOptional.isEmpty) {
            return Optional.empty()
        }
        val user = userOptional.get()
        user.broadcasts.add(broadcast)
        repository.save(user)
        return Optional.of(broadcast)
    }

    override fun updateBroadcast(id: UUID, broadcastId: Int, broadcast: SerializedBroadcast): Optional<SerializedBroadcast> {
        val userOptional = getUserById(id)
        if (userOptional.isEmpty) {
            return Optional.empty()
        }
        val user = userOptional.get()
        user.broadcasts.removeIf { it.id == broadcastId }
        val bc = SerializedBroadcast(broadcastId, broadcast)
        user.broadcasts.add(bc)
        repository.save(user)
        return Optional.of(bc)
    }

    override fun deleteBroadcast(id: UUID, broadcastId: Int): Optional<SerializedBroadcast> {
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
}