package me.oriharel.seriemanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SeriesManagerApplication

fun main(args: Array<String>) {
    runApplication<SeriesManagerApplication>(*args)
}
