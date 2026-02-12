package de.maggiwuerze.xdccwebloader.events.bot

import de.maggiwuerze.xdccwebloader.events.EntityCreationEvent
import de.maggiwuerze.xdccwebloader.model.entity.Bot

class TargetBotCreationEvent(source: Any, bot: Bot) : EntityCreationEvent<Bot>(source, bot)