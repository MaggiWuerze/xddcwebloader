package de.maggiwuerze.xdccloader.model.entity;

import de.maggiwuerze.xdccloader.model.download.DownloadSort;
import de.maggiwuerze.xdccloader.model.forms.UserSettingsForm;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;

	@Column(nullable = false)
	LocalDateTime creationDate = LocalDateTime.now();

	@Min(value = 1L, message = "Refresh rate may not be shorter than 1 second")
	@Column(nullable = false)
	Long refreshrateInSeconds = 1L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	DownloadSort downloadSortBy = DownloadSort.PROGRESS;

	@Min(value = 1, message = "Session Timeout may not be shorter than 1 second")
	@Column(nullable = false)
	Long sessionTimeout = 300L;

	@Size(min=1, message = "Download path may not be empty")
	@Column(nullable = false)
	String downloadPath = "xdcc/";

	public UserSettings(Long refreshrateInSeconds, Long sessionTimeout) {
		this.refreshrateInSeconds = refreshrateInSeconds;
		this.sessionTimeout = sessionTimeout;
	}

	public void update(UserSettingsForm newSettings) {
		setDownloadSortBy(newSettings.getDownloadSortBy());
		setRefreshrateInSeconds(newSettings.getRefreshrateInSeconds());
		setSessionTimeout(newSettings.getSessionTimeout());
		setDownloadPath(newSettings.getDownloadPath());
	}

}
