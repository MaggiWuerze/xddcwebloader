package de.maggiwuerze.xdccloader.events.server;

import de.maggiwuerze.xdccloader.events.EntityUpdateEvent;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.Server;

public class ServerUpdateEvent extends EntityUpdateEvent<Server> {

    public ServerUpdateEvent(Object source, Server server) {
        super(source, server);
    }

}