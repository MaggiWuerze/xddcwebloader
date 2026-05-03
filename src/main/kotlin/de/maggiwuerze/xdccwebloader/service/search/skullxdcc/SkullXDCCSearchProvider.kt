package de.maggiwuerze.xdccwebloader.service.search.skullxdcc

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.maggiwuerze.xdccwebloader.model.search.SearchResult
import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import de.maggiwuerze.xdccwebloader.service.search.base.SearchClient
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngineTO
import de.maggiwuerze.xdccwebloader.service.search.base.SearchProvider
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class SkullXDCCSearchProvider(
    override val searchClient: SearchClient,
    override val name: String = "Skull XDCC"
) : SearchProvider {

    var url: String = "https://skullxdcc.com/ws.php?sterm=%s&"

    override fun search(searchTerm: String, pageable: Pageable): SearchResult {

        searchClient.searchRaw(
            baseUrlTemplate = url,
            searchTerm = searchTerm,
            limitResults = pageable.pageSize,
            page = pageable.pageNumber
        ).let { result ->

            jacksonObjectMapper().readValue(result, Model.SearchResponse::class.java).let { result ->
                val items =  result.data.map {
                    SearchResultItem(
                        server = it.network,
                        serverUrl = it.network,
                        channel = it.channel,
                        bot = it.bot,
                        fileRefId = it.packnum,
                        fileName = it.fname,
                        fileSize = it.fsize,
                    )
                }

                return getPaginatedResult(items, pageable)

            }

        }

    }

    override fun toTO(): SearchEngineTO {
        return SearchEngineTO(name)
    }

}

