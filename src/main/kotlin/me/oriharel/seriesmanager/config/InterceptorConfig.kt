package me.oriharel.seriesmanager.config

import me.oriharel.seriesmanager.interceptor.SameUserCheckInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig : WebMvcConfigurer {
    @Autowired
    private lateinit var sameUserCheckInterceptor: SameUserCheckInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(sameUserCheckInterceptor)
                .addPathPatterns(
                        "/api/v1/user/**/watchtime/**",
                        "/api/v1/user/**/broadcast/**",
                        "/api/v1/user/**/recommend/**",
                )
    }
}