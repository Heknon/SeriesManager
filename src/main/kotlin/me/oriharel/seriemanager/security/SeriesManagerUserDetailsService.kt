package me.oriharel.seriemanager.security

import me.oriharel.seriemanager.dao.user.UserRepository
import me.oriharel.seriemanager.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class SeriesManagerUserDetailsService : UserDetailsService {

    @Autowired
    lateinit var repository: UserRepository

    override fun loadUserByUsername(email: String): UserDetails {
        val user: User = repository.findByUsername(email)
        return CurrentUser(user)
    }

}