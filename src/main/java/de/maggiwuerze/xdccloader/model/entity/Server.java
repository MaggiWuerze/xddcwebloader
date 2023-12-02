package de.maggiwuerze.xdccloader.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
