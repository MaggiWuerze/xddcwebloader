package de.maggiwuerze.xdccloader.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	@Column(nullable = false)
	String name;

	@Column(nullable = false)
	String pattern;

	@Column(nullable = false)
	LocalDateTime creationDate = LocalDateTime.now();

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
