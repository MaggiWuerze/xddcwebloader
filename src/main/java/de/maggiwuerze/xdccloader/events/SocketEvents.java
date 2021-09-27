package de.maggiwuerze.xdccloader.events;

public enum SocketEvents {

	NEW_DOWNLOAD("/newDownload"),
	UPDATED_DOWNLOAD("/updateDownload"),
	DELETED_DOWNLOAD("/deleteDownload"),

	NEW_BOT("/newBot"),
	UPDATED_BOT("/updateBot"),
	DELETED_BOT("/deleteBot"),

	NEW_SERVER("/newServer"),
	UPDATED_SERVER("/updateServer"),
	DELETED_SERVER("/deleteServer"),

	NEW_CHANNEL("/newChannel"),
	UPDATED_CHANNEL("/updateChannel"),
	DELETED_CHANNEL("/deleteChannel"),

	SESSION_TIMEOUT("/timeout");


	String route;

	SocketEvents(String route) {
		this.route = route;
	}

	public String getRoute() {
		return route;
	}
}
