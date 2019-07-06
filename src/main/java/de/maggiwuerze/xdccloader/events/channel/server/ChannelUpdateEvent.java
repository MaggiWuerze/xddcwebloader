package de.maggiwuerze.xdccloader.events.channel.server;

import de.maggiwuerze.xdccloader.events.EntityUpdateEvent;
import de.maggiwuerze.xdccloader.model.Channel;
import de.maggiwuerze.xdccloader.model.Server;

public class ChannelUpdateEvent extends EntityUpdateEvent<Channel> {

    public ChannelUpdateEvent(Object source, Channel channel) {
        super(source, channel);
    }

}