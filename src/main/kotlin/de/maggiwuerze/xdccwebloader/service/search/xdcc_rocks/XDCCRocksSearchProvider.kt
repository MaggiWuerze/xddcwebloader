package de.maggiwuerze.xdccwebloader.service.search.xdcc_rocks

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import de.maggiwuerze.xdccwebloader.persistence.entity.Bot
import de.maggiwuerze.xdccwebloader.persistence.entity.Channel
import de.maggiwuerze.xdccwebloader.persistence.entity.Server
import de.maggiwuerze.xdccwebloader.service.search.base.SearchClient
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngine
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngineTO
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class XDCCRocksSearchProvider(
    override val searchClient: SearchClient,
    override val name: String = "XDCC.Rocks"
) : SearchEngine {

    private val objectMapper = jacksonObjectMapper()

    var url: String = "https://xdcc.rocks/search/?searchword=%s&page=1"

    /**
     * search contains a list of servers, each containing a list of channels, each containing a list of bots, which in turn contain a list of files
     * search (servers[])
     *    └─ channels[]
     *       └─ bots[]
     *         └─ files[]
     */

    //server fields
    var serverHost: String = "serverhost"
    var channelList: String = "channels"
    var serverName: String = "servername"

    //channel fields
    var channelName: String = "channelname"
    var bots: String = "bots"

    //bot fields
    var files: String = "files"
    var botName: String = "botname"
    var fileRefId: String = "packnumber"

    //files fiels
    var file: String = "file"
    var fileName: String = "filename"
    var fileSize: String = "filesize"

    override fun search(searchTerm: String, pageable: Pageable): List<SearchResultItem> {

        val result = searchClient.searchRaw(
            baseUrlTemplate = url,
            searchTerm = searchTerm,
            limitResults = pageable.pageSize,
            page = pageable.pageNumber
        )
        val dto: Model.SearchResponse = objectMapper.readValue(result, Model.SearchResponse::class.java)


        val resultItems = mutableListOf<SearchResultItem>()

        dto.results.forEach { jsonServer ->
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

                        (resultItems).add(
                            SearchResultItem(
                                fileRefId = jsonFile.packnumber,
                                fileName = jsonFile.file.filename,
                                fileSize = jsonFile.file.filesize,
                                server = server.name,
                                channel = channel.name,
                                bot = bot.name
                            )
                        )
                    }

                }

            }
        }

        return resultItems
    }

    override fun toTO(): SearchEngineTO {
        return SearchEngineTO(name)
    }

}

