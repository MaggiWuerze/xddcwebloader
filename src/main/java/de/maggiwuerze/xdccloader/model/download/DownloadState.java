package de.maggiwuerze.xdccloader.model.download;

public enum DownloadState {

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

	DownloadState(String externalString) {

		this.externalString = externalString;
	}

	public String getExternalString() {
		return externalString;
	}
}