package de.maggiwuerze.xdccwebloader.events

enum class SocketEvents(route: String) {
    NEW_DOWNLOAD("/newDownload"),
    UPDATED_DOWNLOAD("/updateDownload"),
    DELETED_DOWNLOAD("/deleteDownload"),
    CANCELLED_DOWNLOAD("/cancelDownload"),
    SESSION_TIMEOUT("/timeout");


    var route: String?

    init {
        this.route = route
    }
}
