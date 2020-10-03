package me.oriharel.seriesmanager.api.user

import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/user/{userId}/recommend")
class UserRecommendationsController {
    @GetMapping(path = ["/"])
    fun getRecommendations(
            @PathVariable("userId") userId: UUID,
            @RequestParam similar: Boolean,
            @RequestParam similarPage: Int,
            @RequestParam recommendations: Boolean,
            @RequestParam recommendationsPage: Int,
            @RequestParam top: Boolean,
            @RequestParam topPage: Int,
            @RequestParam popular: Boolean,
            @RequestParam popularPage: Int,
    ) {

    }

}