package de.maggiwuerze.xdccloader.util

enum class State(val externalString : String) {

    PREPARING("Preparing"),
    PREPARED("Prepared"),
    READY("Ready"),
    CONNECTING("Connecting"),
    TRANSMITTING("Transmitting"),
    FINALIZING("Finalizing"),
    DONE("Done"),
    ERROR("Error code: '%s'"),
    UNKNOWN("Unknown")
}