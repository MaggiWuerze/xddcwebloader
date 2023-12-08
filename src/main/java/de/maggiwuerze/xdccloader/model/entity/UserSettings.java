package de.maggiwuerze.xdccloader.model.entity;

import de.maggiwuerze.xdccloader.model.download.DownloadSort;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
