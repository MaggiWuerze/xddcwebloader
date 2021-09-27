package de.maggiwuerze.xdccloader.events.channel.server;

import de.maggiwuerze.xdccloader.events.EntityDoneEvent;
import de.maggiwuerze.xdccloader.model.entity.Channel;

public class ChannelDoneEvent extends EntityDoneEvent<Channel> {

	public ChannelDoneEvent(Object source, Channel channel) {
		super(source, channel);
	}
}