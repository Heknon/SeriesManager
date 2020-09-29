package me.oriharel.seriemanager.service

import me.oriharel.seriemanager.dao.PersonDao
import me.oriharel.seriemanager.model.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonService @Autowired constructor(@Qualifier("dao") private val personDao: PersonDao) {
    fun addPerson(person: Person): Int {
        return personDao.insertPerson(person)
    }

    fun getAllPeople(): List<Person> {
        return personDao.selectAllPeople()
    }

    fun getPersonById(id: UUID): Optional<Person> {
        return personDao.selectPersonById(id)
    }

    fun deletePersonById(id: UUID): Boolean {
        return personDao.deletePersonById(id)
    }

    fun updatePersonById(id: UUID, person: Person): Boolean {
        return personDao.updatePersonById(id, person)
    }
}