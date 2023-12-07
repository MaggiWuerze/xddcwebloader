package de.maggiwuerze.xdccloader.model.entity;

import de.maggiwuerze.xdccloader.model.download.DownloadSort;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	Boolean initialized = false;

	@Column(nullable = false)
	LocalDateTime creationDate = LocalDateTime.now();

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	DownloadSort downloadSortBy = DownloadSort.PROGRESS;

	@Column(nullable = false)
	Long sessionTimeout = 300L;
}
