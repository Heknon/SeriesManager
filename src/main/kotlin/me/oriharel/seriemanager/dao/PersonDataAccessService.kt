package me.oriharel.seriemanager.dao

import kotlinx.coroutines.selects.select
import me.oriharel.seriemanager.model.Person
import org.springframework.stereotype.Repository
import java.util.*

@Repository("dao")
class PersonDataAccessService : PersonDao {
    override fun insertPerson(id: UUID, person: Person): Int {
        DB.add(Person(id, person.name))
        return 1
    }

    override fun selectAllPeople(): List<Person> {
        return DB
    }

    override fun selectPersonById(id: UUID): Optional<Person> {
        return DB.stream().filter { it.id == id }.findFirst()
    }

    override fun deletePersonById(id: UUID): Boolean {
        val person = selectPersonById(id)
        if (person.isEmpty) {
            return false
        }
        DB.remove(person.get())
        return true
    }

    override fun updatePersonById(id: UUID, person: Person): Boolean {
        return selectPersonById(id).map {
            val index = DB.indexOf(it)
            if (index >= 0) {
                DB[index] = Person(id, person)
                return@map true
            }
            return@map false
        }.orElse(false)
    }

    companion object {
        val DB = arrayListOf<Person>()
    }
}