package de.maggiwuerze.xdccwebloader.service.search.base

import de.maggiwuerze.xdccwebloader.model.search.SearchResult
import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Pageable

interface SearchProvider {
    val searchClient: SearchClient
    val name: String

    fun search(
        searchTerm: String,
        pageable: Pageable = Pageable.ofSize(25).withPage(1)
    ): SearchResult

    fun toTO(): SearchEngineTO

    fun getPaginatedResult(items : List<SearchResultItem>, pageable: Pageable) : SearchResult {
        val from = pageable.pageNumber * pageable.pageSize
        var to = from + pageable.pageSize

        if (to > items.size) {
            to = items.size
        }

        return SearchResult(items.size, items.subList(from, to))
    }
}

@Schema(name = "SearchEngineTO", requiredProperties = ["name"])
class SearchEngineTO(var name: String)


