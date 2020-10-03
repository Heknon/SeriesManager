package me.oriharel.seriesmanager.security

import me.oriharel.seriesmanager.dao.user.UserRepository
import me.oriharel.seriesmanager.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class SeriesManagerUserDetailsService : UserDetailsService {

    @Autowired
    lateinit var repository: UserRepository

    override fun loadUserByUsername(email: String): UserDetails? {
        val user: User = repository.findByUsername(email) ?: return null
        return CurrentUser(user)
    }

}