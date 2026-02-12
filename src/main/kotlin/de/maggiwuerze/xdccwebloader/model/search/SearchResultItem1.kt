package de.maggiwuerze.xdccwebloader.model.search

import java.util.*

abstract class SearchResultItem {
    var id: UUID = UUID.randomUUID()
    var server: String? = null
    var channel: String? = null
    var bot: String? = null
}
