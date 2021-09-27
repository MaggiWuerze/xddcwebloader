package de.maggiwuerze.xdccloader.model.forms;

import lombok.Value;

@Value
public class DownloadForm {

	private final Long targetBotId;
	private final String fileRefId;
}
