package de.maggiwuerze.xdccloader.events.server;

import de.maggiwuerze.xdccloader.events.EntityDoneEvent;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.Server;

public class ServerDoneEvent extends EntityDoneEvent<Server>{

    public ServerDoneEvent(Object source, Server server) {
        super(source, server);
    }

}