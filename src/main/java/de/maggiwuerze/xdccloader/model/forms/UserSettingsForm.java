package de.maggiwuerze.xdccloader.model.forms;

import de.maggiwuerze.xdccloader.model.download.DownloadSort;
import lombok.Value;

@Value
public class UserSettingsForm {

	DownloadSort downloadSortBy;

	Long sessionTimeout;
}
