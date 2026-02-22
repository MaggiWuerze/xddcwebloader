package de.maggiwuerze.xdccwebloader.service.search.skullxdcc

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import de.maggiwuerze.xdccwebloader.service.search.base.SearchClient
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngine
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngineTO
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class SkullXDCCSearchProvider(
    override val searchClient: SearchClient,
    override val name: String = "Skull XDCC"
) : SearchEngine {

    var url: String = "https://skullxdcc.com/ws.php?sterm=%s&"

    override fun search(searchTerm: String, pageable: Pageable): List<SearchResultItem> {

        val result = searchClient.searchRaw(
            baseUrlTemplate = url,
            searchTerm = searchTerm,
            limitResults = pageable.pageSize,
            page = pageable.pageNumber
        )

        jacksonObjectMapper().readValue(result, Model.SearchResponse::class.java).let { result ->

            return result.data.map {
                SearchResultItem(
                    server = it.network,
                    channel = it.channel,
                    bot = it.bot,
                    fileRefId = it.packnum,
                    fileName = it.fname,
                    fileSize = it.fsize,
                )

            }
        }
    }

    override fun toTO(): SearchEngineTO {
        return SearchEngineTO(name)
    }

}

