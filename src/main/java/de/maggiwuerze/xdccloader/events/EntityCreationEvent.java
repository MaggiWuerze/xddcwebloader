package de.maggiwuerze.xdccloader.events;

import org.springframework.context.ApplicationEvent;

public class EntityCreationEvent<T> extends ApplicationEvent {

	private final T payload;

	public EntityCreationEvent(Object source, T payload) {
		super(source);
		this.payload = payload;
	}

	public T getPayload() {
		return payload;
	}
}