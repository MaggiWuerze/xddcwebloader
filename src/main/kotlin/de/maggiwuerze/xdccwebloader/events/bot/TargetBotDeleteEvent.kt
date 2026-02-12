package de.maggiwuerze.xdccwebloader.events.bot

import de.maggiwuerze.xdccwebloader.events.EntityDeleteEvent
import de.maggiwuerze.xdccwebloader.model.entity.Bot

class TargetBotDeleteEvent(source: Any, bot: Bot) : EntityDeleteEvent<Bot>(source, bot)