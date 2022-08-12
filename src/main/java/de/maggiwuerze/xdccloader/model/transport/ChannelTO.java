package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.entity.Channel;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class ChannelTO {

	Long id;

	String name;

	public ChannelTO (Channel channel) {
		this.id = channel.getId();
		this.name = channel.getName();
	}

}