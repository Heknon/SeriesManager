package me.oriharel.seriesmanager.api.user

import io.swagger.v3.oas.annotations.Operation
import lombok.NonNull
import me.oriharel.seriesmanager.api.response.AuthRequest
import me.oriharel.seriesmanager.model.User
import me.oriharel.seriesmanager.security.CurrentUser
import me.oriharel.seriesmanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("api/v1/auth")
class AuthController @Autowired constructor(private val userService: UserService) {
    @Operation(summary = "Get a JWT access token")
    @PostMapping("/login")
    fun generateJwtToken(@Valid @NonNull @RequestBody authRequest: AuthRequest): MutableMap<String, String> {
        if (CurrentUser.isLoggedIn) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logout in order to login!")
        }
        return mutableMapOf(Pair("token", userService.generateJwtToken(authRequest)))
    }

    /**
     * @param user the user to add to the database
     * Adds a user to the database
     */
    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    fun addUser(@Valid @NotNull @RequestBody user: User): Map<Any, Any> {
        if (CurrentUser.isLoggedIn && !CurrentUser.currentUserIsAdmin) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logout in order to register a new account!")
        }
        val newUser = userService.addUser(user)
        val authReq = AuthRequest(newUser.username, user.password, true)
        return mutableMapOf(Pair("token", userService.generateJwtToken(authReq)), Pair("user", newUser))
    }
}