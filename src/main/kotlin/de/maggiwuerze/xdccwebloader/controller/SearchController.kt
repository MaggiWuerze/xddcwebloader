package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.model.download.DownloadTO
import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import de.maggiwuerze.xdccwebloader.service.DownloadService
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngine
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngineTO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("search/")
internal class SearchController(
    private val searchProviders: List<SearchEngine>,
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
        //TODO: add the search result as download, creating bots/channel/server if necessary

        return ResponseEntity(downloadService.create(searchResult).toTO(), HttpStatus.OK)
    }

    @GetMapping("{providerName}/{query}")
    fun searchWithProvider(
        @PathVariable providerName: String,
        @PathVariable query: String
    ): ResponseEntity<List<SearchResultItem>> {

        searchProviders.firstOrNull { it.name == providerName }?.let { searchEngine ->
            return ResponseEntity(searchEngine.search(query), HttpStatus.NOT_FOUND)
        }

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}