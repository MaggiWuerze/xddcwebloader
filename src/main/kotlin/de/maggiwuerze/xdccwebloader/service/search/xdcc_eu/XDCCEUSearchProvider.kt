package de.maggiwuerze.xdccwebloader.service.search.xdcc_eu

import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import de.maggiwuerze.xdccwebloader.service.search.base.SearchClient
import de.maggiwuerze.xdccwebloader.service.search.base.SearchEngineTO
import de.maggiwuerze.xdccwebloader.service.search.base.SearchProvider
import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component


@Component
class XDCCEUSearchProvider(
    override val searchClient: SearchClient,
    override val name: String = "XDCC.EU"
) : SearchProvider {

    var url: String = "https://www.xdcc.eu/search.php?searchkey=%s"

    override fun search(searchTerm: String, pageable: Pageable): List<SearchResultItem> {

        searchClient.searchRaw(
            baseUrlTemplate = url,
            searchTerm = searchTerm,
            limitResults = pageable.pageSize,
            page = pageable.pageNumber
        ).let { result ->

            Jsoup.connect(url).get().let { html ->

                return html.select(".pinakaki tbody tr").map { tablerow ->
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
            }
        }
    }

    override fun toTO(): SearchEngineTO {
        return SearchEngineTO(name)
    }

}

