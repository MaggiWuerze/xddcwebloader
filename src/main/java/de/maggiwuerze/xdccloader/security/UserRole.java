package de.maggiwuerze.xdccloader.security;

public enum UserRole {

    USER("USER"),
    ADMIN("ADMIN");

    String externalString;

    UserRole(String externalString) {

        this.externalString = externalString;
    }

    public String getExternalString() {
        return externalString;
    }
}