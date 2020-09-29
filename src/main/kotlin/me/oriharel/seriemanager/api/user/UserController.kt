package me.oriharel.seriemanager.api.user

import me.oriharel.seriemanager.model.User
import me.oriharel.seriemanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("api/v1/user")
class UserController @Autowired constructor(private val userService: UserService) {
    /**
     * @param id the id of the user to fetch
     * Fetches the data of a certain user.
     */
    @GetMapping(path = ["/{id}"])
    fun getUser(@PathVariable id: UUID): User {
        val optional = userService.getUserById(id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        return optional.get()
    }

    /**
     * Gets all users on the database and displays them.
     */
    @GetMapping
    fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }

    /**
     * @param user the user to add to the database
     * Adds a user to the database
     */
    @PostMapping
    fun addUser(@Valid @NotNull @RequestBody user: User): User {
        return userService.addUser(user)
    }

    /**
     * @param id the id of the user to delete
     * Deletes the user. If the user is not found a NOT FOUND http status code is raised.
     */
    @DeleteMapping(path = ["/{id}"])
    fun deleteUserById(@PathVariable id: UUID): User {
        return userService.deleteUserById(id)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find with the ID given. Resource not deleted.") }
    }

    /**
     * @param id the id of the user to update
     * @param user the user object with the new information.
     * The user parameter passed is cloned and given the id passed, afterwards, the new user created (the clone) is saved to the database.
     * Since this is the functionality, you can also create a user using this method.
     */
    @PutMapping(path = ["/{id}"])
    fun updateUserById(@PathVariable id: UUID, @Valid @NotNull @RequestBody user: User): User {
        return userService.updateUserById(id, user)
    }
}