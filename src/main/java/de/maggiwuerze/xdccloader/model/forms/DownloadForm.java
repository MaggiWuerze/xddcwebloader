package de.maggiwuerze.xdccloader.model.forms;

import lombok.Value;

@Value
public class DownloadForm {
    final private Long targetBotId;
    final private String fileRefId;
}
