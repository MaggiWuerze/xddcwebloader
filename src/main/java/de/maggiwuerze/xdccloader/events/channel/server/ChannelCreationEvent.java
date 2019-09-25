package de.maggiwuerze.xdccloader.events.channel.server;

import de.maggiwuerze.xdccloader.events.EntityCreationEvent;
import de.maggiwuerze.xdccloader.model.entity.Channel;

public class ChannelCreationEvent extends EntityCreationEvent<Channel> {

    public ChannelCreationEvent(Object source, Channel channel) {
        super(source, channel);
    }

}