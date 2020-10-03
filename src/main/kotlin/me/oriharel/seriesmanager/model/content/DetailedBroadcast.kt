package me.oriharel.seriesmanager.model.content

import me.oriharel.seriesmanager.model.entity.company.ProductionCompany

interface DetailedBroadcast {
    val homepage: String
    val productionCompanies: List<ProductionCompany>
    val status: String
}