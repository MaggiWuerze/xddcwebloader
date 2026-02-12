package de.maggiwuerze.xdccwebloader.events.bot

import de.maggiwuerze.xdccwebloader.events.EntityUpdateEvent
import de.maggiwuerze.xdccwebloader.model.entity.Bot

class TargetBotUpdateEvent(source: Any, bot: Bot) : EntityUpdateEvent<Bot>(source, bot)