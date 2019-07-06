package de.maggiwuerze.xdccloader.events.server;

import de.maggiwuerze.xdccloader.events.EntityDeleteEvent;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.Server;

public class ServerDeleteEvent extends EntityDeleteEvent<Server> {

    public ServerDeleteEvent(Object source, Server server) {
        super(source, server);
    }

}