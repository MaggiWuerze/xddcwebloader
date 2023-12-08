package de.maggiwuerze.xdccloader.model.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Server {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false)
	String name;

	@Column(nullable = false)
	String serverUrl;

	@Column(nullable = false)
	LocalDateTime creationDate = LocalDateTime.now();

	public Server(String name, String serverUrl) {
		this.name = name;
		this.serverUrl = serverUrl;
	}
}
