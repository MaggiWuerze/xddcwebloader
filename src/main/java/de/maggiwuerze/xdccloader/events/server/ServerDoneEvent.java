package de.maggiwuerze.xdccloader.events.server;

import de.maggiwuerze.xdccloader.events.EntityDoneEvent;
import de.maggiwuerze.xdccloader.model.entity.Server;

public class ServerDoneEvent extends EntityDoneEvent<Server> {

	public ServerDoneEvent(Object source, Server server) {
		super(source, server);
	}
}