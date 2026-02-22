package de.maggiwuerze.xdccwebloader.service.search.base

import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Pageable

interface SearchEngine {
    val searchClient: SearchClient
    val name: String

    fun search(
        searchTerm: String,
        pageable: Pageable = Pageable.ofSize(25).withPage(1)
    ): List<SearchResultItem>

    fun toTO(): SearchEngineTO
}

@Schema(name = "SearchEngineTO", requiredProperties = ["name"])
class SearchEngineTO(var name: String)

