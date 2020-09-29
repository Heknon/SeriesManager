package me.oriharel.seriemanager.dao.user

import me.oriharel.seriemanager.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository : MongoRepository<User, UUID>