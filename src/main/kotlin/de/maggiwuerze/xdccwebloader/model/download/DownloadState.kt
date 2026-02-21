package de.maggiwuerze.xdccwebloader.model.download

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class DownloadState(externalString: String) {
    CONNECTING("Connecting"),
    DONE("Done"),
    ERROR("Error"),
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

    /**
     * This is what will be sent over REST + WebSocket as the enum "value".
     * So Swagger/OpenAPI will also list these strings as the enum values.
     */
    @JsonValue
    fun toJson(): String = externalString

    companion object {
        /**
         * Allows incoming JSON like "Transmitting" to be parsed back to DownloadState.TRANSMITTING
         */
        @JvmStatic
        @JsonCreator
        fun fromJson(value: String): DownloadState =
            entries.firstOrNull { it.externalString.equals(value, ignoreCase = true) }
                ?: UNKNOWN
    }
}