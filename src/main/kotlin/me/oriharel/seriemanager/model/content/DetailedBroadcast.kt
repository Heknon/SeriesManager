package me.oriharel.seriemanager.model.content

import me.oriharel.seriemanager.model.entity.Language
import me.oriharel.seriemanager.model.entity.company.ProductionCompany

interface DetailedBroadcast {
    val homepage: String
    val productionCompanies: List<ProductionCompany>
    val runtime: Int
    val languages: List<Language>
    val status: String
}