package me.oriharel.seriemanager.model

import com.fasterxml.jackson.annotation.JsonProperty
import me.oriharel.seriemanager.model.content.UserSerializedBroadcast
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.validation.constraints.NotNull

@Document(collection = "users")
class User(
        @Id @JsonProperty val id: UUID?,
        @NotNull @JsonProperty val email: String,
        @NotNull @JsonProperty val name: String,
        @JsonProperty val broadcasts: MutableSet<UserSerializedBroadcast> = mutableSetOf(),
        @NotNull @JsonProperty val username: String,
        @NotNull val password: String,
) {
    constructor(id: UUID, user: me.oriharel.seriemanager.model.User) : this(id, user.email, user.name, user.broadcasts, user.username, user.password)
    constructor() : this(null, "", "", mutableSetOf(), "", "")
}