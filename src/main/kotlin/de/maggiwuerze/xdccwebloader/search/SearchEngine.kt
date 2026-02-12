package de.maggiwuerze.xdccwebloader.search

import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem

abstract class SearchEngine {
    var provider: SearchProvider? = null

    abstract fun search(): MutableList<SearchResultItem?>?
}
