package de.maggiwuerze.xdccloader.events.bot;

import de.maggiwuerze.xdccloader.events.EntityDeleteEvent;
import de.maggiwuerze.xdccloader.model.entity.Bot;

public class TargetBotDeleteEvent extends EntityDeleteEvent<Bot> {

	public TargetBotDeleteEvent(Object source, Bot bot) {
		super(source, bot);
	}
}