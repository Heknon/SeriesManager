package me.oriharel.seriesmanager.model.content

import me.oriharel.seriesmanager.dao.broadcast.SearchType
import me.oriharel.seriesmanager.model.entity.company.ProductionCompany

interface DetailedBroadcast {
    val homepage: String
    val productionCompanies: List<ProductionCompany>
    val status: String
    var lists: Set<String>?
    val mediaType: String

    val searchType get() = if (mediaType.equals("movie", ignoreCase = true)) SearchType.Movie else SearchType.Tv
}