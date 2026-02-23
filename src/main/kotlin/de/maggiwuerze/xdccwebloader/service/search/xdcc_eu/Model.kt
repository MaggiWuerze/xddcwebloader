package de.maggiwuerze.xdccwebloader.service.search.xdcc_eu

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class Model {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SearchResponse(
        val error: Boolean,
        val data: List<ResultItemDto>,
        val total_results: Int,
        val pages: Int
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ResultItemDto(
        val network: String,
        val channel: String,
        val bot: String,
        val packnum: String,
        val fname: String,
        val fsize: String,
    )

}