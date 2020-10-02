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
) {
    constructor(user: User) : this(user.id, user.email, user.name, user.broadcasts)
    constructor(id: UUID, user: User) : this(id, user.email, user.name, user.broadcasts)
    constructor() : this(null, "", "", mutableSetOf())
}