package de.maggiwuerze.xdccloader.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Server {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Size(min=1, message = "Server name must be at least 1 character long")
	@Column(nullable = false)
	String name;

	@Size(min=1, message = "Server URL must be at least 1 character long")
	@Column(nullable = false)
	String serverUrl;

	@Column(nullable = false)
	LocalDateTime creationDate = LocalDateTime.now();

	public Server(String name, String serverUrl) {
		this.name = name;
		this.serverUrl = serverUrl;
	}

	public Server() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
}
