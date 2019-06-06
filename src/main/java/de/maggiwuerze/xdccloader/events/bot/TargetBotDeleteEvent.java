package de.maggiwuerze.xdccloader.events.bot;

import de.maggiwuerze.xdccloader.events.EntityDeleteEvent;
import de.maggiwuerze.xdccloader.events.EntityDoneEvent;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.TargetBot;

public class TargetBotDeleteEvent extends EntityDeleteEvent<TargetBot> {

    public TargetBotDeleteEvent(Object source, TargetBot targetBot) {
        super(source, targetBot);
    }

}