package de.maggiwuerze.xdccloader.model.forms;

import lombok.Value;

@Value
public class TargetBotForm {
    final private String name;
    final private String pattern;
    final private Long serverId;
    final private Long channelId;
    final private Long maxParallelDownloads;
}
