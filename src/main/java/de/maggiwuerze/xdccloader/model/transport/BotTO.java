package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
