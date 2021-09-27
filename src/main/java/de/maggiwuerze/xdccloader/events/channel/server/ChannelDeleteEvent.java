package de.maggiwuerze.xdccloader.events.channel.server;

import de.maggiwuerze.xdccloader.events.EntityDeleteEvent;
import de.maggiwuerze.xdccloader.model.entity.Channel;

public class ChannelDeleteEvent extends EntityDeleteEvent<Channel> {

	public ChannelDeleteEvent(Object source, Channel channel) {
		super(source, channel);
	}
}