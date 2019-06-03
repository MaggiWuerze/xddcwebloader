package de.maggiwuerze.xdccloader.util;

public enum SocketEvents {

    NEW("/newDownload"),
    UPDATE("/updateDownload"),
    DELETE("/deleteDownload");

    String route;

    SocketEvents(String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }
}
