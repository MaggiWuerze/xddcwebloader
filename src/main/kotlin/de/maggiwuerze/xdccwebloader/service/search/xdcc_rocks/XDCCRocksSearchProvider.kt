package de.maggiwuerze.xdccwebloader.service.search.xdcc_rocks

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.maggiwuerze.xdccwebloader.model.search.SearchResult
import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import de.maggiwuerze.xdccwebloader.persistence.entity.Bot
import de.maggiwuerze.xdccwebloader.persistence.entity.Channel
import de.maggiwuerze.xdccwebloader.persistence.entity.Server
import de.maggiwuerze.xdccwebloader.service.search.base.SearchClient
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngineTO
import de.maggiwuerze.xdccwebloader.service.search.base.SearchProvider
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class XDCCRocksSearchProvider(
    override val searchClient: SearchClient,
    override val name: String = "XDCC.Rocks"
) : SearchProvider {

    private val objectMapper = jacksonObjectMapper()

    var url: String = "https://xdcc.rocks/search/?searchword=%s&page=1"

    override fun search(searchTerm: String, pageable: Pageable): SearchResult {

        val result = searchClient.searchRaw(
            baseUrlTemplate = url,
            searchTerm = searchTerm,
            limitResults = pageable.pageSize,
            page = pageable.pageNumber
        )
        val resultItems = mutableListOf<SearchResultItem>()

        jacksonObjectMapper().readValue(result, Model.SearchResponse::class.java).let { result ->
            result.results.forEach { jsonServer ->
                jsonServer.channels.forEach { jsonChannel ->
                    jsonChannel.bots.forEach { jsonBot ->
                        jsonBot.files.forEach { jsonFile ->

                            val server = Server(name = jsonServer.servername, serverUrl = jsonServer.serverhost)
                            val channel = Channel(name = jsonChannel.channelname)
                            val bot = Bot(
                                name = jsonBot.botname,
                                server = server,
                                channel = channel,
                                pattern = "xdcc send %s"
                            )

                            //TODO: change SearchResultItem to return an actual ServerTO,
                            // to allow displaying the name in the table while returning the url with the download request
                            (resultItems).add(
                                SearchResultItem(
                                    fileRefId = jsonFile.packnumber,
                                    fileName = jsonFile.file.filename,
                                    fileSize = jsonFile.file.filesize,
                                    server = server.name,
                                    serverUrl = server.serverUrl,
                                    channel = channel.name,
                                    bot = bot.name
                                )
                            )
                        }
                    }
                }
            }
        }
        return getPaginatedResult(resultItems, pageable)
    }

    override fun toTO(): SearchEngineTO {
        return SearchEngineTO(name)
    }

}

