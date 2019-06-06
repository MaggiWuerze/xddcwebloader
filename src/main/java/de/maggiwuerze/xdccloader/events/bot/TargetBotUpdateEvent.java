package de.maggiwuerze.xdccloader.events.bot;

import de.maggiwuerze.xdccloader.events.EntityUpdateEvent;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.TargetBot;

public class TargetBotUpdateEvent extends EntityUpdateEvent<TargetBot> {

    public TargetBotUpdateEvent(Object source, TargetBot targetBot) {
        super(source, targetBot);
    }

}