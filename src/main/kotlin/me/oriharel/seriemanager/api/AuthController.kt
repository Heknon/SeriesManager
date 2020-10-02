package me.oriharel.seriemanager.api

import lombok.NonNull
import me.oriharel.seriemanager.api.response.AuthRequest
import me.oriharel.seriemanager.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/auth")
class AuthController @Autowired constructor(private val userService: UserService) {
    @PostMapping("/login")
    fun generateJwtToken(@Valid @NonNull @RequestBody authRequest: AuthRequest): MutableMap<String, String> {
        return mutableMapOf(Pair("token", userService.generateJwtToken(authRequest)))
    }
}