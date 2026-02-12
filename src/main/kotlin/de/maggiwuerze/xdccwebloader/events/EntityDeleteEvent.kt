package de.maggiwuerze.xdccwebloader.events

import org.springframework.context.ApplicationEvent

open class EntityDeleteEvent<T>(source: Any, val payload: T) : ApplicationEvent(source)