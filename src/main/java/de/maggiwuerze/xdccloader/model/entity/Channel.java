package de.maggiwuerze.xdccloader.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Channel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Size(min=1, message = "Channel name must be at least 1 character long")
	@Column(nullable = false)
	String name;

	@Column(nullable = false)
	LocalDateTime date = LocalDateTime.now();

	public Channel() {
	}

	public Channel(String name) {
		this.name = name;
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}