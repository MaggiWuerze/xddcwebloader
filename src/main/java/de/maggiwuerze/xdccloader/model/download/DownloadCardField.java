package de.maggiwuerze.xdccloader.model.download;

public enum DownloadCardField {

	ID("id", false),
	BOT("bot", false),
	FILENAME("name", false),
	FILEREFID("fileRefId", false),
	PROGRESS("progress", false),
	SPEED("averageSpeed", false),
	SIZE("filesize", false),
	STATUS("status", false),
	STATUSMESSAGE("statusMessage", false),
	REMAININGTIME("timeRemaining", false);

	String externalString;
	Boolean visible;

	DownloadCardField(String externalString, Boolean visible) {

		this.externalString = externalString;
		this.visible = visible;
	}

	public String getExternalString() {
		return externalString;
	}

	public Boolean getVisible() {
		return visible;
	}
}