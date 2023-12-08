package de.maggiwuerze.xdccloader.model.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Bot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne
	@JoinColumn(name = "CHANNEL_ID")
	Channel channel;

	@ManyToOne
	@JoinColumn(name = "SERVER_ID")
	Server server;

	@Size(min=1, message = "Bot name must be at least 1 character long")
	@Column(nullable = false)
	String name;

	@Size(min=1, message = "Bot pattern must be at least 1 character long")
	@Column(nullable = false)
	String pattern;

	@Column(nullable = false)
	LocalDateTime creationDate = LocalDateTime.now();


	@Min(1L)
	@Column(nullable = false)
	Long maxParallelDownloads = 3L;

	public Bot(Server server, Channel channel, String name, String pattern, Long maxParallelDownloads) {
		this.name = name;
		this.pattern = pattern;
		this.server = server;
		this.channel = channel;
		this.maxParallelDownloads = maxParallelDownloads;
	}
}
