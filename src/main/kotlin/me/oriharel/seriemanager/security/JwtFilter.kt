package me.oriharel.seriemanager.security

import me.oriharel.seriemanager.service.SeriesManagerUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtUtility: JwtUtility

    @Autowired
    private lateinit var userDetailsService: SeriesManagerUserDetailsService

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, filter: FilterChain) {
        val authHeader = req.getHeader("Authorization")
        var username: String? = null
        var token: String? = null

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7)
            try {
                username = jwtUtility.extractUsername(token)
            } catch (e: Exception) {
                res.sendError(1, "Internal Server Error - Cannot validate JWT")
            }
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (jwtUtility.validateToken(token!!, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(req)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filter.doFilter(req, res)
    }
}