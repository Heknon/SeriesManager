package me.oriharel.seriesmanager.interceptor

import me.oriharel.seriesmanager.security.CurrentUser
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SameUserCheckInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val givenUserId = UUID.fromString((request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<*, *>)["userId"] as String)
        if (CurrentUser.currentUserIsAdmin || CurrentUser.currentUserId == givenUserId) {
            return true
        }
        response.sendError(401, "You must use your own user ID")
        return false
    }
}