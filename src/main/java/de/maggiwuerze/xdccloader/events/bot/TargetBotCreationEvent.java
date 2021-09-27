package de.maggiwuerze.xdccloader.events.bot;

import de.maggiwuerze.xdccloader.events.EntityCreationEvent;
import de.maggiwuerze.xdccloader.model.entity.Bot;

public class TargetBotCreationEvent extends EntityCreationEvent<Bot> {

	public TargetBotCreationEvent(Object source, Bot bot) {
		super(source, bot);
	}
}