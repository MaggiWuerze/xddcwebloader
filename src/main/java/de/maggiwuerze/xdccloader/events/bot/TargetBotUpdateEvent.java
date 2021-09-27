package de.maggiwuerze.xdccloader.events.bot;

import de.maggiwuerze.xdccloader.events.EntityUpdateEvent;
import de.maggiwuerze.xdccloader.model.entity.Bot;

public class TargetBotUpdateEvent extends EntityUpdateEvent<Bot> {

	public TargetBotUpdateEvent(Object source, Bot bot) {
		super(source, bot);
	}
}