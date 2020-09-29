package me.oriharel.seriemanager.model.entity.company

import me.oriharel.seriemanager.model.entity.Entity

interface Company : Entity {
    val logo: String
    val originCountry: String
}