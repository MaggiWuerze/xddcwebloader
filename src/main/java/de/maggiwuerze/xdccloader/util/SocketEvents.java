package de.maggiwuerze.xdccloader.util;

public enum SocketEvents {

    NEW_DOWNLOAD("/newDownload"),
    UPDATED_DOWNLOAD("/updateDownload"),
    DELETED_DOWNLOAD("/deleteDownload"),

    NEW_SERVER("/newServer"),
    UPDATED_SERVER("/updateServer"),
    DELETED_SERVER("/deleteServer"),

    NEW_CHANNEL("/newChannel"),
    UPDATED_CHANNEL("/updateChannel"),
    DELETED_CHANNEL("/deleteChannel");

    String route;

    SocketEvents(String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }
}
