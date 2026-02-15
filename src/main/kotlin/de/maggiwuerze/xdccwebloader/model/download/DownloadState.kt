package de.maggiwuerze.xdccwebloader.model.download

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class DownloadState(externalString: String) {
    CONNECTING("Connecting"),
    DONE("Done"),
    ERROR("Error : '%s'"),
    FINALIZING("Finalizing"),
    PREPARING("Preparing"),
    PREPARED("Prepared"),
    READY("Ready"),
    RESTARTING("Restarting"),
    STOPPED("Stopped"),
    TRANSMITTING("Transmitting"),
    UNKNOWN("Unknown");

    var externalString: String

    init {
        this.externalString = externalString
    }
}