package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Value;

@Value
public class BotTO {

	Long id;

	String channel;

	String server;

	String name;

	String pattern;

	Long maxParallelDownloads;

	public BotTO(Bot bot) {
		this.id = bot.getId();
		this.name = bot.getName();
		this.pattern = bot.getPattern();
		this.server = bot.getServer().getName();
		this.channel = bot.getChannel().getName();
		this.maxParallelDownloads = bot.getMaxParallelDownloads();
	}
}
