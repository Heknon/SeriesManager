package me.oriharel.seriemanager.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.NotBlank

data class Person(
        @JsonProperty("id") val id: UUID?,
        @NotBlank @JsonProperty("name") val name: String
) {
    constructor(person: Person) : this(person.id, person.name)
    constructor(id: UUID, person: Person) : this(id, person.name)
}