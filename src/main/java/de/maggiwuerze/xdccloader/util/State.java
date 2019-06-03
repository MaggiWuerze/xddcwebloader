package de.maggiwuerze.xdccloader.util;

public enum State {

    PREPARING("Preparing"),
    PREPARED("Prepared"),
    READY("Ready"),
    CONNECTING("Connecting"),
    TRANSMITTING("Transmitting"),
    FINALIZING("Finalizing"),
    DONE("Done"),
    ERROR("Error code: '%s'"),
    UNKNOWN("Unknown");

    String externalString;

    State(String externalString) {

        this.externalString = externalString;
    }
}