package de.maggiwuerze.xdccloader.util;

public enum State {

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

    String externalString;

    State(String externalString) {

        this.externalString = externalString;
    }

    public String getExternalString() {
        return externalString;
    }
}