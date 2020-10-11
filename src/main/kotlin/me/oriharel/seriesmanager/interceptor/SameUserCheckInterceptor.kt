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
        if (CurrentUser.currentUserIsAdmin || CurrentUser.isLoggedIn) {
            return true
        }
        response.sendError(401, "You must use your own user ID")
        return false
    }
}