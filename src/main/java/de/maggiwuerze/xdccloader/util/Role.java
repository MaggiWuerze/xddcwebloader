package de.maggiwuerze.xdccloader.util;

public enum Role {

    USER("USER"),
    ADMIN("ADMIN");

    String externalString;

    Role(String externalString) {

        this.externalString = externalString;
    }

    public String getExternalString() {
        return externalString;
    }
}