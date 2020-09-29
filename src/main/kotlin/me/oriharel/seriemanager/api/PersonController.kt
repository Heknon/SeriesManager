package me.oriharel.seriemanager.api

import me.oriharel.seriemanager.model.Person
import me.oriharel.seriemanager.service.PersonService
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.validation.Valid

@RequestMapping("api/v1/person")
@RestController
class PersonController @Autowired constructor(private val personService: PersonService) {
    @PostMapping
    fun addPerson(@Valid @NotNull @RequestBody person: Person) {
        personService.addPerson(person)
    }

    @GetMapping
    fun getAllPeople(): List<Person> {
        return personService.getAllPeople()
    }

    @GetMapping(path = ["{id}"])
    fun getPersonById(@PathVariable("id") id: UUID): Person? {
        return personService.getPersonById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found") }
    }

    @DeleteMapping(path = ["{id}"])
    fun deletePersonById(@PathVariable("id") id: UUID) {
        if (!personService.deletePersonById(id)) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found")
    }

    @PutMapping(path = ["{id}"])
    fun updatePersonById(@PathVariable("id") id: UUID, @Valid @NotNull @RequestBody person: Person) {
        if (!personService.updatePersonById(id, person)) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found")
    }
}