package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.model.download.DownloadTO
import de.maggiwuerze.xdccwebloader.model.search.SearchResult
import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import de.maggiwuerze.xdccwebloader.service.DownloadService
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngineTO
import de.maggiwuerze.xdccwebloader.service.search.base.SearchProvider
import org.slf4j.LoggerFactory
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/search/")
internal class SearchController(
    private val searchProviders: List<SearchProvider>,
    private val downloadService: DownloadService
) {

    val log = LoggerFactory.getLogger(this.javaClass.name)

    @GetMapping
    fun listSearchProviders(): ResponseEntity<List<SearchEngineTO>> {
        return ResponseEntity(searchProviders.map { it.toTO() }.toList(), HttpStatus.OK)
    }

    @PostMapping()
    fun startDownloadFromSearchResult(
        @RequestBody searchResult: SearchResultItem
    ): ResponseEntity<DownloadTO> {
        return ResponseEntity(downloadService.create(searchResult).toTO(), HttpStatus.OK)
    }

    @GetMapping("{providerName}/{query}/{page}/{pageSize}")
    fun searchWithProvider(
        @PathVariable providerName: String,
        @PathVariable query: String,
        @PathVariable page : Int = 1,
        @PathVariable pageSize: Int = 25
    ): ResponseEntity<SearchResult> {
        return searchProviders.firstOrNull { it.name == providerName }?.let { searchEngine ->
            ResponseEntity(searchEngine.search(query, Pageable.ofSize(pageSize).withPage(page)), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}