package de.maggiwuerze.xdccloader.events.bot;

import de.maggiwuerze.xdccloader.events.EntityCreationEvent;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.TargetBot;

public class TargetBotCreationEvent extends EntityCreationEvent<TargetBot> {

    public TargetBotCreationEvent(Object source, TargetBot targetBot) {
        super(source, targetBot);
    }

}