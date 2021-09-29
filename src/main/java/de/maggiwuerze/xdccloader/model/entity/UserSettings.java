package de.maggiwuerze.xdccloader.model.entity;

import de.maggiwuerze.xdccloader.model.download.DownloadSort;
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

	@Column(nullable = false)
	Long refreshrateInSeconds = 1L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	DownloadSort downloadSortBy = DownloadSort.PROGRESS;

	@Column(nullable = false)
	Long sessionTimeout = 300L;

	public UserSettings(Long refreshrateInSeconds, Long sessionTimeout) {
		this.refreshrateInSeconds = refreshrateInSeconds;
		this.sessionTimeout = sessionTimeout;
	}


}
