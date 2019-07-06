package de.maggiwuerze.xdccloader.events.channel.server;

import de.maggiwuerze.xdccloader.events.EntityDoneEvent;
import de.maggiwuerze.xdccloader.model.Channel;
import de.maggiwuerze.xdccloader.model.Server;

public class ChannelDoneEvent extends EntityDoneEvent<Channel>{

    public ChannelDoneEvent(Object source, Channel channel) {
        super(source, channel);
    }

}