package me.oriharel.seriemanager.api.user

import io.swagger.v3.oas.annotations.Operation
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
    @Operation(summary = "Get a user's data from it's ID")
    @GetMapping(path = ["/{id}"])
    fun getUser(@PathVariable id: UUID): User {
        val optional = userService.getUserById(id)
        if (optional.isEmpty) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        return optional.get()
    }

    /**
     * Gets all users on the database and displays them.
     */
    @Operation(summary = "Get all users registered")
    @GetMapping
    fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }

    /**
     * @param user the user to add to the database
     * Adds a user to the database
     */
    @Operation(summary = "Register a new user")
    @PostMapping
    fun addUser(@Valid @NotNull @RequestBody user: User): User {
        return userService.addUser(user)
    }

    /**
     * @param id the id of the user to delete
     * Deletes the user. If the user is not found a NOT FOUND http status code is raised.
     */
    @Operation(summary = "Remove a user from database - delete user")
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
    @Operation(summary = "Update a user's data")
    @PutMapping(path = ["/{id}"])
    fun updateUserById(@PathVariable id: UUID, @Valid @NotNull @RequestBody user: User): User {
        return userService.updateUserById(id, user)
    }
}