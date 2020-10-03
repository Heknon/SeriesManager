package me.oriharel.seriesmanager.model.entity.company

import me.oriharel.seriesmanager.model.entity.Entity

interface Company : Entity {
    val logo: String?
    val originCountry: String
}