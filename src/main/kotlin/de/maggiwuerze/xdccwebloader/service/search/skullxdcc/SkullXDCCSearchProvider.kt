package de.maggiwuerze.xdccwebloader.service.search.skullxdcc

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

    //result fields
    var botName: String = "bot"
    var fileRefId: String = "packnum"
    var fileName: String = "fname"
    var fileSize: String = "fsize"
    var channelName: String = "channel"
    var serverName: String = "network"

    override fun search(searchTerm: String, pageable: Pageable): List<SearchResultItem> {

        val result = searchClient.search(
            baseUrlTemplate = url,
            searchTerm = searchTerm,
            limitResults = pageable.pageSize,
            page = pageable.pageNumber
        )
        val resultItems = mutableListOf<SearchResultItem>()

        result["data"].values().forEach {
            resultItems.add(
                SearchResultItem(
                    server = it[serverName].asText(),
                    channel = it[channelName].asText(),
                    bot = it[botName].asText(),
                    fileRefId = it[fileRefId].asText(),
                    fileName = it[fileName].asText(),
                    fileSize = it[fileSize].asText(),
                )
            )
        }

        return resultItems
    }

    override fun toTO(): SearchEngineTO {
        return SearchEngineTO(name)
    }

}

