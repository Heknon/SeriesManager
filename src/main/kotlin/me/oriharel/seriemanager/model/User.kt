package me.oriharel.seriemanager.model

import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriemanager.dao.user.UserRepository
import me.oriharel.seriemanager.model.content.UserSerializedBroadcast
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

@Document(collection = "users")
class User(
        @Id @JsonProperty val id: UUID?,
        @NotNull @JsonProperty val username: String,
        @NotNull @JsonProperty val password: String,
        @NotNull @JsonProperty val email: String,
        @NotNull @JsonProperty val name: String,
        @Null @JsonProperty val isAdmin: Boolean,
        @JsonProperty val broadcasts: MutableSet<UserSerializedBroadcast> = mutableSetOf(),
) {
    constructor(id: UUID, user: User) : this(id, user.username, user.password, user.email, user.name, user.isAdmin, user.broadcasts)
}