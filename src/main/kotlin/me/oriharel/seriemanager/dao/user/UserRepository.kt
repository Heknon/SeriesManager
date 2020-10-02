package me.oriharel.seriemanager.dao.user

import me.oriharel.seriemanager.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface UserRepository : MongoRepository<User, UUID> {
    @Query("{ 'username' : ?0 }")
    fun findByUsername(username: String): User
}