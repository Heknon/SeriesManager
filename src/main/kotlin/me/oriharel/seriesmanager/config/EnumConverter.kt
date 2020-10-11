package me.oriharel.seriesmanager.config

import me.oriharel.seriesmanager.dao.broadcast.SearchType
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class EnumConverter : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(object : Converter<String, SearchType> {
            override fun convert(str: String): SearchType {
                println("str: $str")
                println(SearchType.valueOf(str.capitalize()))
                return SearchType.valueOf(str.capitalize())
            }
        })
    }
}