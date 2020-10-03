package me.oriharel.seriesmanager.security

import me.oriharel.seriesmanager.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

class CurrentUser(
        val dbUser: User,
        val data: Map<String, Any> = mutableMapOf(),
        extraAuthorities: List<GrantedAuthority> = mutableListOf(),
) : org.springframework.security.core.userdetails.User(
        dbUser.username,
        dbUser.password,
        true,
        true,
        true,
        true,
        mutableListOf(
                GrantedAuthority { if (dbUser.isAdmin) "ADMIN" else "USER" },
                *extraAuthorities.toTypedArray()
        )
) {
    val authorityMap: Map<String, GrantedAuthority> = authorities.associateBy { it.authority }

    companion object {
        val isLoggedIn: Boolean
            get() {
                val principal = SecurityContextHolder.getContext().authentication.principal
                return principal !is String || principal != "anonymousUser"
            }
        val isNotLoggedIn: Boolean get() = !isLoggedIn
        val currentUser: CurrentUser
            get() {
                return SecurityContextHolder.getContext().authentication.principal as CurrentUser
            }
        val currentUserId get() = currentUser.dbUser.id
        val currentUserIsAdmin: Boolean
            get() {
                if (!isLoggedIn) return false
                return currentUser.authorityMap.containsKey("ADMIN")
            }
    }
}