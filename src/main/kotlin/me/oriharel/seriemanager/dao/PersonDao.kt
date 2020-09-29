package me.oriharel.seriemanager.dao

import me.oriharel.seriemanager.model.Person
import java.util.*

interface PersonDao {
    fun insertPerson(id: UUID, person: Person): Int

    fun insertPerson(person: Person): Int {
        val id = UUID.randomUUID()
        return insertPerson(id, person)
    }

    fun selectAllPeople(): List<Person>

    fun selectPersonById(id: UUID): Optional<Person>

    fun deletePersonById(id: UUID): Boolean

    fun updatePersonById(id: UUID, person: Person): Boolean
}