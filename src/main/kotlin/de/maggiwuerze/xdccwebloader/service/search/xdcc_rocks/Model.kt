package de.maggiwuerze.xdccwebloader.service.search.xdcc_rocks

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class Model {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SearchResponse(
        val responsecode: String,
        val results: List<ServerDto>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ServerDto(
        val servername: String,
        val serverhost: String,
        val channels: List<ChannelDto>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ChannelDto(
        val channelname: String,
        val bots: List<BotDto>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class BotDto(
        val botname: String,
        val files: List<FileWrapperDto>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class FileWrapperDto(
        val packnumber: String,
        val file: FileDto
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class FileDto(
        val filename: String,
        val filesize: String,
        val score: String
    )

}