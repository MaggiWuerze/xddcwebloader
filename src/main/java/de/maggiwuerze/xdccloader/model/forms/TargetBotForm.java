package de.maggiwuerze.xdccloader.model.forms;

import lombok.Value;

@Value
public class TargetBotForm {

	private final String name;
	private final String pattern;
	private final Long serverId;
	private final Long channelId;
	private final Long maxParallelDownloads = 3L;
}
