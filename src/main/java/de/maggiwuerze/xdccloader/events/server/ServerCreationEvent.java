package de.maggiwuerze.xdccloader.events.server;

import de.maggiwuerze.xdccloader.events.EntityCreationEvent;
import de.maggiwuerze.xdccloader.model.entity.Server;

public class ServerCreationEvent extends EntityCreationEvent<Server> {

	public ServerCreationEvent(Object source, Server server) {
		super(source, server);
	}
}