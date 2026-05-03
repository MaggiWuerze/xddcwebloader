package de.maggiwuerze.xdccwebloader.service.search.xdcc_eu

import de.maggiwuerze.xdccwebloader.model.search.SearchResult
import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import de.maggiwuerze.xdccwebloader.service.search.base.SearchClient
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngineTO
import de.maggiwuerze.xdccwebloader.service.search.base.SearchProvider
import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.to


@Component
class XDCCEUSearchProvider(
    override val searchClient: SearchClient,
    override val name: String = "XDCC.EU"
) : SearchProvider {

    var url: String = "https://www.xdcc.eu/search.php?searchkey=%s"

    val cache = ConcurrentHashMap<String, List<SearchResultItem>>()

    override fun search(searchTerm: String, pageable: Pageable): SearchResult {

        cache[searchTerm]?.let {

            return getPaginatedResult(it, pageable)

        } ?: searchClient.searchRaw(
            baseUrlTemplate = url,
            searchTerm = searchTerm,
            limitResults = pageable.pageSize,
            page = pageable.pageNumber
        )?.let { html ->

            val result = Jsoup.parse(html).let { parsedHtml ->

                val result = parsedHtml.select(".pinakaki tbody tr").map { tablerow ->
                    val serverName = tablerow.children()[0].text()
                    val serverURL = tablerow.children()[1].children()[1].attr("href")
                    val channelname = tablerow.children()[1].text()
                    val botname = tablerow.children()[2].text()
                    val fileRefId = tablerow.children()[3].text()
                    val fileSize = tablerow.children()[5].text()
                    val fileName = tablerow.children()[6].text()

                    SearchResultItem(
                        fileRefId = fileRefId,
                        fileName = fileName,
                        fileSize = fileSize,
                        server = serverName,
                        serverUrl = StringUtils.substringBetween(serverURL, "irc://", "/"),
                        channel = channelname,
                        bot = botname
                    )
                }
                return@let result
            }

            cache[searchTerm] = result
            return@let getPaginatedResult(result, pageable)
        }

        return SearchResult()
    }

    override fun toTO(): SearchEngineTO {
        return SearchEngineTO(name)
    }

}

