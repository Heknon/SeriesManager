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
@RequestMapping("api/v1/user", produces = ["application/json"], consumes = ["application/json"])
class UserController @Autowired constructor(private val userService: UserService) {
    /**
     * @param userId the id of the user to fetch
     * Fetches the data of a certain user.
     */
    @Operation(summary = "Get a user's data from it's ID")
    @GetMapping(path = ["/{userId}"])
    fun getUser(@PathVariable userId: UUID): User {
        val optional = userService.getUserById(userId)
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
     * @param userId the id of the user to delete
     * Deletes the user. If the user is not found a NOT FOUND http status code is raised.
     */
    @Operation(summary = "Remove a user from database - delete user")
    @DeleteMapping(path = ["/{userId}"])
    fun deleteUserById(@PathVariable userId: UUID): User {
        return userService.deleteUserById(userId)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find with the ID given. Resource not deleted.") }
    }

    /**
     * @param userId the id of the user to update
     * @param user the user object with the new information.
     * The user parameter passed is cloned and given the id passed, afterwards, the new user created (the clone) is saved to the database.
     * Since this is the functionality, you can also create a user using this method.
     */
    @Operation(summary = "Update a user's data")
    @PutMapping(path = ["/{userId}"])
    fun updateUserById(
            @PathVariable userId: UUID,
            @Valid @NotNull @RequestBody user: User,
    ): User {
        return userService.updateUserById(userId, user)
    }
}